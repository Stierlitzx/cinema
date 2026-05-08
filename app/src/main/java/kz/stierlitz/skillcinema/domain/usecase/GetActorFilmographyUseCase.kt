package kz.stierlitz.skillcinema.domain.usecase

import kz.stierlitz.skillcinema.domain.model.Movie
import kz.stierlitz.skillcinema.domain.repository.ActorRepository
import kz.stierlitz.skillcinema.data.remote.mapper.toFilm
import javax.inject.Inject

class GetActorFilmographyUseCase @Inject constructor(
    private val repository: ActorRepository
) {
    suspend operator fun invoke(actorId: Int): Map<String, List<Movie>> {
        val actor = repository.getActorDetail(actorId)
        return actor.films
            ?.groupBy { it.professionKey }
            ?.mapValues { (_, films) ->
                films.distinctBy { it.filmId }.map { it.toFilm() }
            }
            ?: emptyMap()
    }
}
