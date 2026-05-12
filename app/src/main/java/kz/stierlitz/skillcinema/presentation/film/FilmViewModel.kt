package kz.stierlitz.skillcinema.presentation.film

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kz.stierlitz.skillcinema.SkillCinemaApp
import kz.stierlitz.skillcinema.core.network.NetworkModule
import kz.stierlitz.skillcinema.data.local.entity.WatchedMovieEntity
import kz.stierlitz.skillcinema.data.local.entity.CollectionMovieEntity
import kz.stierlitz.skillcinema.data.repository.MovieRepositoryImpl
import kz.stierlitz.skillcinema.domain.repository.MovieRepository

class FilmViewModel : ViewModel() {
    private val repository: MovieRepository = MovieRepositoryImpl(NetworkModule.kinopoiskApi)
    private val watchedMovieDao = SkillCinemaApp.instance.database.watchedMovieDao
    private val collectionDao = SkillCinemaApp.instance.database.collectionDao
    private val collectionMovieDao = SkillCinemaApp.instance.database.collectionMovieDao

    private val _state = MutableStateFlow(FilmContract.State())
    val state: StateFlow<FilmContract.State> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<FilmContract.Effect>()
    val effect: SharedFlow<FilmContract.Effect> = _effect.asSharedFlow()

    fun handleIntent(intent: FilmContract.Intent) {
        when (intent) {
            is FilmContract.Intent.LoadFilm -> loadFilmDetails(intent.filmId)
            is FilmContract.Intent.ToggleCollectionByName -> toggleCollectionByName(intent.collectionName)
            is FilmContract.Intent.ToggleCollectionById -> toggleCollectionById(intent.collectionId)
            is FilmContract.Intent.CreateCollection -> createCollection(intent.name)
        }
    }

    private fun observeCollections(filmId: Int) {
        viewModelScope.launch {
            if (collectionDao.getCollectionCount() == 0) {
                collectionDao.insertCollection(kz.stierlitz.skillcinema.data.local.entity.CollectionEntity(name = "Любимые", isCustom = false))
                collectionDao.insertCollection(kz.stierlitz.skillcinema.data.local.entity.CollectionEntity(name = "Хочу посмотреть", isCustom = false))
            }
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
        viewModelScope.launch {
            collectionMovieDao.getCollectionsForMovie(filmId).collect { movieCollections ->
                _state.update { it.copy(movieCollections = movieCollections) }
            }
        }
    }

    private fun createCollection(name: String) {
        viewModelScope.launch {
            collectionDao.insertCollection(kz.stierlitz.skillcinema.data.local.entity.CollectionEntity(name = name, isCustom = true))
        }
    }

    private fun toggleCollectionByName(name: String) {
        viewModelScope.launch {
            val collection = collectionDao.getCollectionByName(name)
            if (collection != null) {
                toggleCollectionById(collection.id)
            }
        }
    }

    private fun toggleCollectionById(collectionId: Int) {
        viewModelScope.launch {
            val currentMovie = _state.value.film ?: return@launch
            val isIncluded = _state.value.movieCollections.contains(collectionId)
            
            if (isIncluded) {
                collectionMovieDao.removeMovieFromCollection(collectionId, currentMovie.kinopoiskId)
            } else {
                val entity = CollectionMovieEntity(
                    collectionId = collectionId,
                    filmId = currentMovie.kinopoiskId,
                    nameRu = currentMovie.nameRu,
                    nameEn = currentMovie.nameEn,
                    posterUrl = currentMovie.posterUrl,
                    posterUrlPreview = currentMovie.posterUrlPreview,
                    ratingKinopoisk = currentMovie.ratingKinopoisk,
                    ratingImdb = currentMovie.ratingImdb,
                    year = currentMovie.year,
                    genres = currentMovie.genres.joinToString(", ") { it.name }
                )
                collectionMovieDao.insertMovieToCollection(entity)
            }
        }
    }

    private fun loadFilmDetails(id: Int) {
        _state.update { it.copy(isLoading = true, error = null) }
        observeCollections(id)
        viewModelScope.launch {
            try {
                coroutineScope {
                    val filmDeferred = async { repository.getFilm(id) }
                    val staffDeferred = async { repository.getStaff(id) }
                    val imagesDeferred = async { repository.getFilmImages(id) }
                    val seasonsDeferred = async { repository.getSeasons(id) }
                    val similarsDeferred = async {
                        try {
                            repository.getSimilarFilms(id)
                        } catch (e: Exception) {
                            emptyList()
                        }
                    }

                    val seasons = try {
                        seasonsDeferred.await()
                    } catch (e: Exception) {
                        emptyList()
                    }

                    val film = filmDeferred.await()
                    val staff = staffDeferred.await()
                    val images = imagesDeferred.await()
                    val similars = similarsDeferred.await()

                    val actors = staff.filter { it.professionKey == "ACTOR" }
                    val workers = staff.filter { it.professionKey != "ACTOR" }
                    val gallery = images

                    val entity = WatchedMovieEntity(
                        filmId = film.kinopoiskId,
                        nameRu = film.nameRu,
                        nameEn = film.nameEn,
                        posterUrl = film.posterUrl,
                        posterUrlPreview = film.posterUrlPreview,
                        ratingKinopoisk = film.ratingKinopoisk,
                        ratingImdb = film.ratingImdb,
                        year = film.year
                    )
                    watchedMovieDao.insertWatchedMovie(entity)

                    _state.update {
                        it.copy(
                            isLoading = false,
                            film = film,
                            actors = actors,
                            workers = workers,
                            gallery = gallery,
                            seasons = seasons,
                            similars = similars
                        )
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _state.update { it.copy(isLoading = false, error = e.message) }
                _effect.emit(FilmContract.Effect.ShowError(e.message ?: "Network error"))
            }
        }
    }
}
