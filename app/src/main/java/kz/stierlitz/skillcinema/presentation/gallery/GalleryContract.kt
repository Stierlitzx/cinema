package kz.stierlitz.skillcinema.presentation.gallery

import kz.stierlitz.skillcinema.domain.model.FilmImage

sealed class GalleryIntent {
    data class LoadGallery(val filmId: Int) : GalleryIntent()
    data class SelectTab(val index: Int) : GalleryIntent()
    object OnBackClick : GalleryIntent()
}

sealed class GalleryEffect {
    object NavigateBack : GalleryEffect()
    data class ShowError(val message: String) : GalleryEffect()
}

data class GalleryState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val filmId: Int = 0,
    val tabs: List<GalleryTab> = emptyList(),
    val selectedTabIndex: Int = 0,
    val images: List<FilmImage> = emptyList()
)

data class GalleryTab(
    val label: String,
    val typeKey: String,
    val count: Int = 0
)

