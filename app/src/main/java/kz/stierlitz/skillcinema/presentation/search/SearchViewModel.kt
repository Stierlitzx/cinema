package kz.stierlitz.skillcinema.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
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

class SearchViewModel : ViewModel() {
    private val repository: MovieRepository = MovieRepositoryImpl(NetworkModule.kinopoiskApi)

    private val _state = MutableStateFlow(SearchContract.State())
    val state: StateFlow<SearchContract.State> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<SearchContract.Effect>()
    val effect: SharedFlow<SearchContract.Effect> = _effect.asSharedFlow()

    private var searchJob: Job? = null

    fun handleIntent(intent: SearchContract.Intent) {
        when (intent) {
            is SearchContract.Intent.SearchTextChange -> {
                _state.update { it.copy(query = intent.query) }
                searchMovies(intent.query)
            }
        }
    }

    private fun searchMovies(query: String) {
        searchJob?.cancel()
        if (query.isBlank()) {
            _state.update { it.copy(results = emptyList(), isLoading = false, error = null) }
            return
        }

        searchJob = viewModelScope.launch {
            delay(500)
            _state.update { it.copy(isLoading = true, error = null) }
            try {
                val results = repository.getFilmsByFilters(keyword = query)
                _state.update { it.copy(isLoading = false, results = results) }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, error = e.message) }
                _effect.emit(SearchContract.Effect.ShowError(e.message ?: "Unknown error"))
            }
        }
    }
}

