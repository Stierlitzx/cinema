package kz.stierlitz.skillcinema.presentation.filmography

import kz.stierlitz.skillcinema.domain.model.Movie

data class FilmographyState(
    val isLoading: Boolean = false,
    val actorName: String = "",
    val tabs: List<FilmographyTab> = emptyList(),
    val selectedTabIndex: Int = 0,
    val films: List<Movie> = emptyList(),
    val error: String? = null
)

data class FilmographyTab(
    val label: String,
    val professionKey: String,
    val count: Int
)

sealed interface FilmographyEvent {
    data class Load(val actorId: Int, val actorName: String) : FilmographyEvent
    data class SelectTab(val index: Int) : FilmographyEvent
    data class OnFilmClick(val filmId: Int) : FilmographyEvent
    object OnBackClick : FilmographyEvent
}

sealed interface FilmographyEffect {
    data class NavigateToFilm(val filmId: Int) : FilmographyEffect
    object NavigateBack : FilmographyEffect
    data class ShowError(val message: String) : FilmographyEffect
}