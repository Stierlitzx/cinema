package kz.stierlitz.skillcinema.presentation.filmList

import kz.stierlitz.skillcinema.domain.model.Movie

interface FilmListContract {
    data class State(
        val isLoading: Boolean = false,
        val error: String? = null,
        val movies: List<Movie> = emptyList(),
        val listType: String = "",
        val title: String = ""
    )

    sealed class Intent {
        data class LoadFilmList(val type: String, val title: String) : Intent()
    }

    sealed class Effect {
        data class ShowError(val message: String) : Effect()
    }
}