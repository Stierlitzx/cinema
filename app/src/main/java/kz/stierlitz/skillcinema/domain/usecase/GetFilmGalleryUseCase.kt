package kz.stierlitz.skillcinema.domain.usecase

import kz.stierlitz.skillcinema.domain.model.FilmImage
import kz.stierlitz.skillcinema.domain.repository.MovieRepository
import javax.inject.Inject

class GetFilmGalleryUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    suspend operator fun invoke(filmId: Int, type: String, page: Int = 1): List<FilmImage> {
        return repository.getFilmImages(filmId, type, page)
    }
}

