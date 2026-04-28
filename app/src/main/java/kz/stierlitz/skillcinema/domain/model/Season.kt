package kz.stierlitz.skillcinema.domain.model

data class Season(
    val number: Int,
    val episodes: List<Episode>
)

data class Episode(
    val seasonNumber: Int,
    val episodeNumber: Int,
    val nameRu: String?,
    val nameEn: String?,
    val synopsis: String?,
    val releaseDate: String?
)

