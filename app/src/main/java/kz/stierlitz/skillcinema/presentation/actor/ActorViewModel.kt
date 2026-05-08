package kz.stierlitz.skillcinema.presentation.actor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kz.stierlitz.skillcinema.domain.usecase.GetActorDetailUseCase
import kz.stierlitz.skillcinema.domain.usecase.GetActorBestFilmsUseCase
import kz.stierlitz.skillcinema.presentation.actor.ActorState
import kz.stierlitz.skillcinema.presentation.actor.ActorEvent
import kz.stierlitz.skillcinema.presentation.actor.ActorEffect

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kz.stierlitz.skillcinema.core.network.NetworkModule
import kz.stierlitz.skillcinema.data.repository.ActorRepositoryImpl

class ActorViewModel(
    private val getActorDetail: GetActorDetailUseCase = GetActorDetailUseCase(ActorRepositoryImpl(NetworkModule.kinopoiskApi)),
    private val getActorBestFilms: GetActorBestFilmsUseCase = GetActorBestFilmsUseCase(ActorRepositoryImpl(NetworkModule.kinopoiskApi))
) : ViewModel() {

    private val _state = MutableStateFlow(ActorState())
    val state: StateFlow<ActorState> = _state.asStateFlow()

    private val _effect = Channel<ActorEffect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    fun onEvent(event: ActorEvent) {
        when (event) {
            is ActorEvent.LoadActor -> loadActor(event.actorId)
            is ActorEvent.OnFilmographyClick -> {
                val actor = _state.value.actor ?: return
                viewModelScope.launch {
                    _effect.send(
                        ActorEffect.NavigateToFilmography(
                            actorId = actor.personId,
                            actorName = actor.nameRu ?: actor.nameEn ?: ""
                        )
                    )
                }
            }
            is ActorEvent.OnFilmClick -> {
                viewModelScope.launch {
                    _effect.send(ActorEffect.NavigateToFilm(event.filmId))
                }
            }
            ActorEvent.OnBackClick -> {
                viewModelScope.launch {
                    _effect.send(ActorEffect.NavigateBack)
                }
            }
        }
    }

    private fun loadActor(actorId: Int) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            try {
                val actorDetail = getActorDetail(actorId)
                // Best = top 3 by rating from all films list
                val best = getActorBestFilms(actorId)
                _state.update {
                    it.copy(
                        isLoading = false,
                        actor = actorDetail,
                        bestFilms = best
                    )
                }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, error = e.message) }
                _effect.send(ActorEffect.ShowError(e.message ?: "Unknown error"))
            }
        }
    }
}
