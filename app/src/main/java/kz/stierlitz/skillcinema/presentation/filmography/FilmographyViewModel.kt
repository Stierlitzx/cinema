package kz.stierlitz.skillcinema.presentation.filmography

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kz.stierlitz.skillcinema.core.network.NetworkModule
import kz.stierlitz.skillcinema.data.repository.ActorRepositoryImpl
import kz.stierlitz.skillcinema.domain.usecase.GetActorDetailUseCase
import kz.stierlitz.skillcinema.domain.usecase.GetActorFilmographyUseCase
import kz.stierlitz.skillcinema.domain.usecase.GetActorBestFilmsUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FilmographyViewModel(
    private val getActorFilmography: GetActorFilmographyUseCase = GetActorFilmographyUseCase(ActorRepositoryImpl(NetworkModule.kinopoiskApi))
) : ViewModel() {

    private val _state = MutableStateFlow(FilmographyState())
    val state: StateFlow<FilmographyState> = _state.asStateFlow()

    private val _effect = Channel<FilmographyEffect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    fun onEvent(event: FilmographyEvent) {
        when (event) {
            is FilmographyEvent.Load -> loadFilmography(event.actorId, event.actorName)
            is FilmographyEvent.SelectTab -> {
                val stateValue = _state.value
                val selectedTabKey = stateValue.tabs.getOrNull(event.index)?.professionKey
                val filteredFilms = if (selectedTabKey != null) {
                    filmsByProfession[selectedTabKey] ?: emptyList()
                } else emptyList()

                _state.update {
                    it.copy(
                        selectedTabIndex = event.index,
                        films = filteredFilms
                    )
                }
            }
            is FilmographyEvent.OnFilmClick -> {
                viewModelScope.launch {
                    _effect.send(FilmographyEffect.NavigateToFilm(event.filmId))
                }
            }
            FilmographyEvent.OnBackClick -> {
                viewModelScope.launch {
                    _effect.send(FilmographyEffect.NavigateBack)
                }
            }
        }
    }

    private var filmsByProfession: Map<String, List<kz.stierlitz.skillcinema.domain.model.Movie>> = emptyMap()

    private fun loadFilmography(actorId: Int, actorName: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null, actorName = actorName) }
            try {
                filmsByProfession = getActorFilmography(actorId)
                val tabs = filmsByProfession.map { (key, list) ->
                    FilmographyTab(
                        label = translateProfessionKey(key),
                        professionKey = key,
                        count = list.size
                    )
                }

                val defaultIndex = 0
                val defaultKey = tabs.getOrNull(defaultIndex)?.professionKey
                val defaultFilms = if (defaultKey != null) filmsByProfession[defaultKey] ?: emptyList() else emptyList()

                _state.update {
                    it.copy(
                        isLoading = false,
                        tabs = tabs,
                        selectedTabIndex = defaultIndex,
                        films = defaultFilms
                    )
                }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, error = e.message) }
                _effect.send(FilmographyEffect.ShowError(e.message ?: "Unknown error"))
            }
        }
    }

    private fun translateProfessionKey(key: String): String {
        return when(key) {
            "ACTOR" -> "Актёр"
            "DIRECTOR" -> "Режиссер"
            "WRITER" -> "Сценарист"
            "PRODUCER" -> "Продюсер"
            "COMPOSER" -> "Композитор"
            "DESIGNER" -> "Художник"
            "EDITOR" -> "Монтажер"
            "OPERATOR" -> "Оператор"
            "HIMSELF" -> "Играет себя"
            "HERSELF" -> "Играет себя"
            "VOICE_DIRECTOR" -> "Озвучка"
            else -> key
        }
    }
}
