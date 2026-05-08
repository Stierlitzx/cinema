package kz.stierlitz.skillcinema.presentation.film

import kz.stierlitz.skillcinema.domain.model.FilmImage
import kz.stierlitz.skillcinema.domain.model.Staff
import kz.stierlitz.skillcinema.domain.model.Movie
import kz.stierlitz.skillcinema.domain.model.Season
import kz.stierlitz.skillcinema.data.local.entity.CollectionEntity

interface FilmContract {
    data class State(
        val isLoading: Boolean = false,
        val error: String? = null,
        val film: Movie? = null,
        val seasons: List<Season> = emptyList(),
        val actors: List<Staff> = emptyList(),
        val workers: List<Staff> = emptyList(),
        val gallery: List<FilmImage> = emptyList(),
        val similars: List<Movie> = emptyList(),
        val collections: List<CollectionEntity> = emptyList(),
        val movieCollections: List<Int> = emptyList(),
        val collectionCounts: Map<Int, Int> = emptyMap()
    )

    sealed class Intent {
        data class LoadFilm(val filmId: Int) : Intent()
        data class ToggleCollectionByName(val collectionName: String) : Intent()
        data class ToggleCollectionById(val collectionId: Int) : Intent()
        data class CreateCollection(val name: String) : Intent()
    }

    sealed class Effect {
        data class ShowError(val message: String) : Effect()
    }
}
