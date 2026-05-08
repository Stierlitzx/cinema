package kz.stierlitz.skillcinema.presentation.profile

import kz.stierlitz.skillcinema.domain.model.Movie
import kz.stierlitz.skillcinema.data.local.entity.CollectionEntity

interface ProfileContract {
    data class State(
        val username: String? = "",
        val profilePictureUrl: String? = "",
        val isLoading: Boolean = true,
        val watchedMovies: List<Movie> = emptyList(),
        val collections: List<CollectionEntity> = emptyList(),
        val collectionCounts: Map<Int, Int> = emptyMap()
    )

    sealed class Intent {
        object LoadUserData : Intent()
        object OnSignOutClick : Intent()
        object CheckAuth : Intent()
        object ClearHistory : Intent()
        data class CreateCollection(val name: String) : Intent()
        data class DeleteCollection(val id: Int) : Intent()
    }

    sealed class SideEffect {
        object NavigateToRegistration : SideEffect()
    }
}