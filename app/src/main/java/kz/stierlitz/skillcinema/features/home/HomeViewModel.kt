package kz.stierlitz.skillcinema.features.home
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class HomeViewModel : ViewModel() {
    private val _state = MutableStateFlow(HomeContract.State())
    val state: StateFlow<HomeContract.State> = _state.asStateFlow()
    private val _effect = MutableSharedFlow<HomeContract.Effect>()
    val effect: SharedFlow<HomeContract.Effect> = _effect.asSharedFlow()
    init {
        handleIntent(HomeContract.Intent.Load)
    }
    fun handleIntent(intent: HomeContract.Intent) {
        when (intent) {
            is HomeContract.Intent.Load -> loadMovies()
        }
    }
    private fun loadMovies() {
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            try {
                coroutineScope {
                    val api = kz.stierlitz.skillcinema.core.network.NetworkModule.kinopoiskApi
                    val premieresDeferred = async { api.getCollections(type = "CLOSES_RELEASES") }
                    val popularDeferred = async { api.getCollections(type = "TOP_POPULAR_ALL") }
                    val top250Deferred = async { api.getCollections(type = "TOP_250_MOVIES") }
                    val seriesDeferred = async { api.getCollections(type = "POPULAR_SERIES") }
                    val actionUsaDeferred = async { api.getFilmsByFilters(countries = 1, genres = 11) } // 1: USA, 11: Action
                    val dramaDeferred = async { api.getFilmsByFilters(countries = 3, genres = 8) } // 3: France, 8: Drama
                    val premieresResponse = premieresDeferred.await()
                    val popularResponse = popularDeferred.await()
                    val top250Response = top250Deferred.await()
                    val seriesResponse = seriesDeferred.await()
                    val actionUsaResponse = actionUsaDeferred.await()
                    val dramaResponse = dramaDeferred.await()
                    fun mapCollection(items: List<kz.stierlitz.skillcinema.data.remote.kinopoisk.FilmCollectionItem>) = items.map { item ->
                        kz.stierlitz.skillcinema.domain.model.Movie(
                            kinopoiskId = item.kinopoiskId,
                            nameRu = item.nameRu,
                            nameEn = item.nameEn,
                            nameOriginal = item.nameOriginal,
                            posterUrl = item.posterUrl ?: "",
                            posterUrlPreview = item.posterUrlPreview ?: "",
                            ratingKinopoisk = item.ratingKinopoisk,
                            ratingImdb = item.ratingImbd,
                            year = item.year ?: 0,
                            genres = item.genres.map { kz.stierlitz.skillcinema.domain.model.Genre(it.genre ?: "") },
                            reviewsCount = 0,
                            webUrl = "",
                            isTicketsAvailable = false,
                            type = "FILM",
                            lastSync = ""
                        )
                    }
                    fun mapSearch(items: List<kz.stierlitz.skillcinema.data.remote.kinopoisk.FilmSearchItem>) = items.map { item ->
                        kz.stierlitz.skillcinema.domain.model.Movie(
                            kinopoiskId = item.kinopoiskId,
                            nameRu = item.nameRu,
                            nameEn = item.nameEn,
                            nameOriginal = item.nameOriginal,
                            posterUrl = item.posterUrl ?: "",
                            posterUrlPreview = item.posterUrlPreview ?: "",
                            ratingKinopoisk = item.ratingKinopoisk,
                            ratingImdb = item.ratingImdb,
                            year = item.year ?: 0,
                            genres = item.genres.map { kz.stierlitz.skillcinema.domain.model.Genre(it.genre ?: "") },
                            reviewsCount = 0,
                            webUrl = "",
                            isTicketsAvailable = false,
                            type = "FILM",
                            lastSync = ""
                        )
                    }
                    _state.update { it.copy(
                        isLoading = false,
                        premieres = mapCollection(premieresResponse.items),
                        popular = mapCollection(popularResponse.items),
                        top250 = mapCollection(top250Response.items),
                        series = mapCollection(seriesResponse.items),
                        actionUsa = mapSearch(actionUsaResponse.items),
                        drama = mapSearch(dramaResponse.items)
                    ) }
                }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, error = e.message) }
                _effect.emit(HomeContract.Effect.ShowError(e.message ?: "Unknown error"))
            }
        }
    }
}
