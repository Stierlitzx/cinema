package kz.stierlitz.skillcinema.presentation.filmList

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
import kz.stierlitz.skillcinema.core.network.NetworkModule
import kz.stierlitz.skillcinema.data.repository.MovieRepositoryImpl
import kz.stierlitz.skillcinema.domain.repository.MovieRepository

class FilmListViewModel : ViewModel() {
    private val repository: MovieRepository = MovieRepositoryImpl(NetworkModule.kinopoiskApi)

    private val _state = MutableStateFlow(FilmListContract.State())
    val state: StateFlow<FilmListContract.State> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<FilmListContract.Effect>()
    val effect: SharedFlow<FilmListContract.Effect> = _effect.asSharedFlow()

    fun handleIntent(intent: FilmListContract.Intent) {
        when (intent) {
            is FilmListContract.Intent.LoadFilmList -> loadList(intent.type, intent.title)
        }
    }

    private fun loadList(type: String, title: String) {
        _state.update { it.copy(isLoading = true, listType = type, title = title, error = null) }
        viewModelScope.launch {
            try {
                val movies = when (type) {
                    "CLOSES_RELEASES", "TOP_POPULAR_ALL", "TOP_250_MOVIES", "POPULAR_SERIES" -> {
                        repository.getCollections(type = type)
                    }
                    "ACTION_USA" -> {
                        repository.getFilmsByFilters(countries = 1, genres = 11)
                    }
                    "DRAMA_FRANCE" -> {
                        repository.getFilmsByFilters(countries = 3, genres = 8)
                    }
                    else -> emptyList()
                }

                _state.update { it.copy(isLoading = false, movies = movies) }
            } catch (e: Exception) {
                e.printStackTrace()
                _state.update { it.copy(isLoading = false, error = e.message) }
                _effect.emit(FilmListContract.Effect.ShowError(e.message ?: "Unknown error"))
            }
        }
    }
}
