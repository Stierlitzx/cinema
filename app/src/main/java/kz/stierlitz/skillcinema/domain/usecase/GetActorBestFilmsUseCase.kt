package kz.stierlitz.skillcinema.domain.usecase

import kz.stierlitz.skillcinema.domain.model.Movie
import kz.stierlitz.skillcinema.domain.repository.ActorRepository
import kz.stierlitz.skillcinema.data.remote.mapper.toFilm
import javax.inject.Inject

class GetActorBestFilmsUseCase @Inject constructor(
    private val repository: ActorRepository
) {
    suspend operator fun invoke(actorId: Int): List<Movie> {
        val actor = repository.getActorDetail(actorId)
        return actor.films
            ?.distinctBy { it.filmId }
            ?.sortedByDescending { it.rating?.toDoubleOrNull() ?: 0.0 }
            ?.take(10)
            ?.map { it.toFilm() }
            ?: emptyList()
    }
}
