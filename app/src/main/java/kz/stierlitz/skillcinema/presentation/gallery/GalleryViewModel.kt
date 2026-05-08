package kz.stierlitz.skillcinema.presentation.gallery

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kz.stierlitz.skillcinema.core.network.NetworkModule
import kz.stierlitz.skillcinema.data.repository.MovieRepositoryImpl
import kz.stierlitz.skillcinema.domain.usecase.GetFilmGalleryUseCase
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kz.stierlitz.skillcinema.domain.model.FilmImage

class GalleryViewModel(
    private val getFilmGallery: GetFilmGalleryUseCase = GetFilmGalleryUseCase(MovieRepositoryImpl(NetworkModule.kinopoiskApi))
) : ViewModel() {

    private val _state = MutableStateFlow(GalleryState())
    val state: StateFlow<GalleryState> = _state.asStateFlow()

    private val _effect = Channel<GalleryEffect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    private var imagesByType: Map<String, List<FilmImage>> = emptyMap()

    fun onEvent(intent: GalleryIntent) {
        when (intent) {
            is GalleryIntent.LoadGallery -> loadOriginal(intent.filmId)
            is GalleryIntent.SelectTab -> selectTab(intent.index)
            GalleryIntent.OnBackClick -> {
                viewModelScope.launch { _effect.send(GalleryEffect.NavigateBack) }
            }
        }
    }

    private fun selectTab(index: Int) {
        val currentTabs = _state.value.tabs
        if (index in currentTabs.indices) {
            val key = currentTabs[index].typeKey
            val newImages = imagesByType[key] ?: emptyList()
            _state.update {
                it.copy(
                    selectedTabIndex = index,
                    images = newImages
                )
            }
        }
    }

    private fun loadOriginal(filmId: Int) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null, filmId = filmId) }
            try {
                coroutineScope {
                    val stillsDeferred = async { getFilmGallery(filmId, "STILL") }
                    val shootingDeferred = async { getFilmGallery(filmId, "SHOOTING") }
                    val postersDeferred = async { getFilmGallery(filmId, "POSTER") }

                    val stills = stillsDeferred.await()
                    val shooting = shootingDeferred.await()
                    val posters = postersDeferred.await()

                    imagesByType = mapOf(
                        "STILL" to stills,
                        "SHOOTING" to shooting,
                        "POSTER" to posters
                    ).filterValues { it.isNotEmpty() }

                    val tabs = imagesByType.map { (key, list) ->
                        GalleryTab(
                            label = translateType(key),
                            typeKey = key,
                            count = list.size
                        )
                    }

                    val defaultIndex = 0
                    val defaultKey = tabs.getOrNull(defaultIndex)?.typeKey
                    val defaultImages = if (defaultKey != null) imagesByType[defaultKey] ?: emptyList() else emptyList()

                    _state.update {
                        it.copy(
                            isLoading = false,
                            tabs = tabs,
                            selectedTabIndex = defaultIndex,
                            images = defaultImages
                        )
                    }
                }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, error = e.message) }
                _effect.send(GalleryEffect.ShowError(e.message ?: "Unknown error"))
            }
        }
    }

    private fun translateType(key: String): String {
        return when(key) {
            "STILL" -> "Кадры"
            "SHOOTING" -> "Со съемок"
            "POSTER" -> "Постеры"
            else -> key
        }
    }
}

