package kz.stierlitz.skillcinema.features.film

import kz.stierlitz.skillcinema.data.remote.kinopoisk.FilmResponse
import kz.stierlitz.skillcinema.data.remote.kinopoisk.StaffResponse
import kz.stierlitz.skillcinema.data.remote.kinopoisk.FilmImageItem
import kz.stierlitz.skillcinema.domain.model.Movie

interface FilmContract {
    data class State(
        val isLoading: Boolean = false,
        val error: String? = null,
        val film: FilmResponse? = null,
        val actors: List<StaffResponse> = emptyList(),
        val workers: List<StaffResponse> = emptyList(),
        val gallery: List<FilmImageItem> = emptyList(),
        val similars: List<Movie> = emptyList()
    )

    sealed class Intent {
        data class LoadFilm(val filmId: Int) : Intent()
    }

    sealed class Effect {
        data class ShowError(val message: String) : Effect()
    }
}
