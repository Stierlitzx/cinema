package kz.stierlitz.skillcinema.presentation.actor

import kz.stierlitz.skillcinema.domain.model.ActorDetail
import kz.stierlitz.skillcinema.domain.model.Movie

data class ActorState(
    val isLoading: Boolean = false,
    val actor: ActorDetail? = null,
    val bestFilms: List<Movie> = emptyList(),
    val error: String? = null
)

sealed interface ActorEvent {
    data class LoadActor(val actorId: Int) : ActorEvent
    object OnFilmographyClick : ActorEvent
    data class OnFilmClick(val filmId: Int) : ActorEvent
    object OnBackClick : ActorEvent
}

sealed interface ActorEffect {
    data class NavigateToFilmography(val actorId: Int, val actorName: String) : ActorEffect
    data class NavigateToFilm(val filmId: Int) : ActorEffect
    object NavigateBack : ActorEffect
    data class ShowError(val message: String) : ActorEffect
}
