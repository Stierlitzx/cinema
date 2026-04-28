package kz.stierlitz.skillcinema.presentation.home

import kz.stierlitz.skillcinema.domain.model.Movie

interface HomeContract {
    data class State(
        val isLoading: Boolean = false,
        val error: String? = null,
        val premieres: List<Movie> = emptyList(),
        val popular: List<Movie> = emptyList(),
        val actionUsa: List<Movie> = emptyList(),
        val top250: List<Movie> = emptyList(),
        val drama: List<Movie> = emptyList(),
        val series: List<Movie> = emptyList()
    )

    sealed class Intent {
        object Load : Intent()
    }

    sealed class Effect {
        data class ShowError(val message: String) : Effect()
    }
}