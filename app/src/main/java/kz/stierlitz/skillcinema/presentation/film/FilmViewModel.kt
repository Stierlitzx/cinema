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
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kz.stierlitz.skillcinema.core.network.NetworkModule
import kz.stierlitz.skillcinema.data.repository.MovieRepositoryImpl
import kz.stierlitz.skillcinema.domain.repository.MovieRepository

class FilmViewModel : ViewModel() {
    private val repository: MovieRepository = MovieRepositoryImpl(NetworkModule.kinopoiskApi)

    private val _state = MutableStateFlow(FilmContract.State())
    val state: StateFlow<FilmContract.State> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<FilmContract.Effect>()
    val effect: SharedFlow<FilmContract.Effect> = _effect.asSharedFlow()

    fun handleIntent(intent: FilmContract.Intent) {
        when (intent) {
            is FilmContract.Intent.LoadFilm -> loadFilmDetails(intent.filmId)
        }
    }

    private fun loadFilmDetails(id: Int) {
        _state.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            try {
                coroutineScope {
                    val filmDeferred = async { repository.getFilm(id) }
                    val staffDeferred = async { repository.getStaff(id) }
                    val imagesDeferred = async { repository.getFilmImages(id) }
                    val seasonsDeferred = async { repository.getSeasons(id) }
                    
                    val seasons = try {
                        seasonsDeferred.await()
                    } catch (e: Exception) {
                        emptyList()
                    }

                    val film = filmDeferred.await()
                    val staff = staffDeferred.await()
                    val images = imagesDeferred.await()

                    val actors = staff.filter { it.professionKey == "ACTOR" }
                    val workers = staff.filter { it.professionKey != "ACTOR" }
                    val gallery = images

                    _state.update {
                        it.copy(
                            isLoading = false,
                            film = film,
                            actors = actors,
                            workers = workers,
                            gallery = gallery,
                            seasons = seasons
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
