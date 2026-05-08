package kz.stierlitz.skillcinema.domain.usecase

import kz.stierlitz.skillcinema.domain.model.ActorDetail
import kz.stierlitz.skillcinema.domain.repository.ActorRepository
import javax.inject.Inject

class GetActorDetailUseCase @Inject constructor(
    private val repository: ActorRepository
) {
    suspend operator fun invoke(actorId: Int): ActorDetail =
        repository.getActorDetail(actorId)
}

