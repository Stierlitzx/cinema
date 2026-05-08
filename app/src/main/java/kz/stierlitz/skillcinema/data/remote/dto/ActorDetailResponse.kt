package kz.stierlitz.skillcinema.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ActorDetailResponse(
    val personId: Int,
    val nameRu: String?,
    val nameEn: String?,
    val posterUrl: String?,
    val profession: String?,
    val films: List<ActorFilmDto>
)

@Serializable
data class ActorFilmDto(
    val filmId: Int,
    val nameRu: String?,
    val nameEn: String?,
    val rating: String?,
    val description: String?,
    val professionKey: String
)
