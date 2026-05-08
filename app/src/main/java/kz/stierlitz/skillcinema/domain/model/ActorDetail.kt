package kz.stierlitz.skillcinema.domain.model

data class ActorDetail(
    val personId: Int,
    val nameRu: String?,
    val nameEn: String?,
    val posterUrl: String?,
    val profession: String?,
    val films: List<ActorFilm>?
)

data class ActorFilm(
    val filmId: Int,
    val nameRu: String?,
    val nameEn: String?,
    val rating: String?,
    val description: String?,
    val professionKey: String
)

