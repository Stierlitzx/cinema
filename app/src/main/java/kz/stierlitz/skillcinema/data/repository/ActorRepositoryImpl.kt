package kz.stierlitz.skillcinema.data.repository

import kz.stierlitz.skillcinema.data.remote.kinopoisk.KinopoiskApi
import kz.stierlitz.skillcinema.data.remote.mapper.toDomain
import kz.stierlitz.skillcinema.domain.model.ActorDetail
import kz.stierlitz.skillcinema.domain.repository.ActorRepository

class ActorRepositoryImpl(
    private val api: KinopoiskApi
) : ActorRepository {
    override suspend fun getActorDetail(actorId: Int): ActorDetail {
        return api.getActorDetail(actorId).toDomain()
    }
}

