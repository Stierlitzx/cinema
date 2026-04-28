package kz.stierlitz.skillcinema.presentation.home
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
import kz.stierlitz.skillcinema.core.network.NetworkModule
import kz.stierlitz.skillcinema.data.repository.MovieRepositoryImpl
import kz.stierlitz.skillcinema.domain.repository.MovieRepository

class HomeViewModel : ViewModel() {
    private val repository: MovieRepository = MovieRepositoryImpl(NetworkModule.kinopoiskApi)

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
                    val premieresDeferred = async { repository.getCollections(type = "CLOSES_RELEASES") }
                    val popularDeferred = async { repository.getCollections(type = "TOP_POPULAR_ALL") }
                    val top250Deferred = async { repository.getCollections(type = "TOP_250_MOVIES") }
                    val seriesDeferred = async { repository.getCollections(type = "POPULAR_SERIES") }
                    val actionUsaDeferred = async { repository.getFilmsByFilters(countries = 1, genres = 11) } // 1: USA, 11: Action
                    val dramaDeferred = async { repository.getFilmsByFilters(countries = 3, genres = 8) } // 3: France, 8: Drama

                    val premieresResponse = premieresDeferred.await()
                    val popularResponse = popularDeferred.await()
                    val top250Response = top250Deferred.await()
                    val seriesResponse = seriesDeferred.await()
                    val actionUsaResponse = actionUsaDeferred.await()
                    val dramaResponse = dramaDeferred.await()

                    _state.update { it.copy(
                        isLoading = false,
                        premieres = premieresResponse,
                        popular = popularResponse,
                        top250 = top250Response,
                        series = seriesResponse,
                        actionUsa = actionUsaResponse,
                        drama = dramaResponse
                    ) }
                }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, error = e.message) }
                _effect.emit(HomeContract.Effect.ShowError(e.message ?: "Unknown error"))
            }
        }
    }
}
