package kz.stierlitz.skillcinema.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kz.stierlitz.skillcinema.SkillCinemaApp
import kz.stierlitz.skillcinema.data.remote.auth.GoogleAuthUiClient
import kz.stierlitz.skillcinema.domain.model.Movie

class ProfileViewModel(
    private val googleAuthUiClient: GoogleAuthUiClient
) : ViewModel() {

    private val watchedMovieDao = SkillCinemaApp.instance.database.watchedMovieDao
    private val collectionDao = SkillCinemaApp.instance.database.collectionDao

    private val _state = MutableStateFlow(ProfileContract.State())
    val state = _state.asStateFlow()

    private val _effect = Channel<ProfileContract.SideEffect>()
    val effect = _effect.receiveAsFlow()

    init {
        handleIntent(ProfileContract.Intent.LoadUserData)
        observeWatchedMovies()
        observeCollections()
    }

    fun handleIntent(intent: ProfileContract.Intent) {
        when (intent) {
            is ProfileContract.Intent.LoadUserData -> loadUserData()
            is ProfileContract.Intent.OnSignOutClick -> signOut()
            is ProfileContract.Intent.ClearHistory -> clearHistory()
            is ProfileContract.Intent.CreateCollection -> createCollection(intent.name)
            is ProfileContract.Intent.DeleteCollection -> deleteCollection(intent.id)
            else -> {}
        }
    }

    private fun observeCollections() {
        viewModelScope.launch {
            if (collectionDao.getCollectionCount() == 0) {
                collectionDao.insertCollection(kz.stierlitz.skillcinema.data.local.entity.CollectionEntity(name = "Любимые", isCustom = false))
                collectionDao.insertCollection(kz.stierlitz.skillcinema.data.local.entity.CollectionEntity(name = "Хочу посмотреть", isCustom = false))
            }
        }

        viewModelScope.launch {
            collectionDao.getAllCollections().collectLatest { collections ->
                _state.update { it.copy(collections = collections) }

                // Create flows for each collection's count
                val countFlows = collections.associate { collection ->
                    collection.id to SkillCinemaApp.instance.database.collectionMovieDao.getMovieCountInCollection(collection.id)
                }

                // Combine all count flows
                if (countFlows.isNotEmpty()) {
                    val flows = countFlows.values.toList()
                    combine(flows) { counts ->
                        val countsMap = mutableMapOf<Int, Int>()
                        countFlows.keys.zip(counts).forEach { (id, count) ->
                            countsMap[id] = count
                        }
                        countsMap
                    }.collectLatest { countsMap ->
                        _state.update { it.copy(collectionCounts = countsMap) }
                    }
                }
            }
        }
    }

    private fun createCollection(name: String) {
        viewModelScope.launch {
            collectionDao.insertCollection(kz.stierlitz.skillcinema.data.local.entity.CollectionEntity(name = name, isCustom = true))
        }
    }

    private fun deleteCollection(id: Int) {
        viewModelScope.launch {
            collectionDao.deleteCustomCollection(id)
        }
    }

    private fun observeWatchedMovies() {
        viewModelScope.launch {
            watchedMovieDao.getWatchedMovies().collect { list ->
                val movies = list.map {
                    Movie(
                        kinopoiskId = it.filmId,
                        nameRu = it.nameRu,
                        nameEn = it.nameEn,
                        posterUrl = it.posterUrl ?: "",
                        posterUrlPreview = it.posterUrlPreview ?: "",
                        ratingKinopoisk = it.ratingKinopoisk,
                        ratingImdb = it.ratingImdb,
                        year = it.year ?: 0,
                        reviewsCount = 0,
                        genres = emptyList(),
                        webUrl = "",
                        isTicketsAvailable = false,
                        type = "FILM",
                        lastSync = ""
                    )
                }
                _state.update { it.copy(watchedMovies = movies) }
            }
        }
    }

    private fun clearHistory() {
        viewModelScope.launch {
            watchedMovieDao.clearWatchedHistory()
        }
    }

    private fun loadUserData() {
        val user = googleAuthUiClient.getSignedInUser()
        _state.update {
            it.copy(
                username = user?.username,
                profilePictureUrl = user?.profilePictureUrl,
                isLoading = false
            )
        }
    }

    private fun signOut() {
        viewModelScope.launch {
            googleAuthUiClient.signOut()
            _effect.send(ProfileContract.SideEffect.NavigateToRegistration)
        }
    }
}