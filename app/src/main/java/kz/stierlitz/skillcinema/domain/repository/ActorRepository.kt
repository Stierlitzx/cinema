package kz.stierlitz.skillcinema.domain.repository

import kz.stierlitz.skillcinema.domain.model.ActorDetail

interface ActorRepository {
    suspend fun getActorDetail(actorId: Int): ActorDetail
}

