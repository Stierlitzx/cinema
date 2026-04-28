package kz.stierlitz.skillcinema.presentation.seasons

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

class SeasonsViewModel : ViewModel() {
    private val repository: MovieRepository = MovieRepositoryImpl(NetworkModule.kinopoiskApi)

    private val _state = MutableStateFlow(SeasonsContract.State())
    val state: StateFlow<SeasonsContract.State> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<SeasonsContract.Effect>()
    val effect: SharedFlow<SeasonsContract.Effect> = _effect.asSharedFlow()

    fun handleIntent(intent: SeasonsContract.Intent) {
        when (intent) {
            is SeasonsContract.Intent.LoadSeasons -> loadSeasons(intent.filmId, intent.filmName)
            is SeasonsContract.Intent.SelectSeason -> _state.update { it.copy(selectedSeasonIndex = intent.index) }
        }
    }

    private fun loadSeasons(id: Int, filmName: String) {
        _state.update { it.copy(isLoading = true, filmName = filmName, error = null) }
        viewModelScope.launch {
            try {
                val seasons = repository.getSeasons(id)
                _state.update { it.copy(isLoading = false, seasons = seasons, selectedSeasonIndex = 0) }
            } catch (e: Exception) {
                e.printStackTrace()
                _state.update { it.copy(isLoading = false, error = e.message) }
                _effect.emit(SeasonsContract.Effect.ShowError(e.message ?: "Unknown error"))
            }
        }
    }
}

