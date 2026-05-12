package kz.stierlitz.skillcinema.presentation.search

import kz.stierlitz.skillcinema.domain.model.Movie
import kz.stierlitz.skillcinema.domain.model.Person

interface SearchContract {
    data class State(
        val isLoading: Boolean = false,
        val query: String = "",
        val movieResults: List<Movie> = emptyList(),
        val personResults: List<Person> = emptyList(),
        val error: String? = null
    )

    sealed class Intent {
        data class SearchTextChange(val query: String) : Intent()
    }

    sealed class Effect {
        data class ShowError(val message: String) : Effect()
    }
}
