package kz.stierlitz.skillcinema.domain.model

data class Person(
    val kinopoiskId: Int,
    val nameRu: String?,
    val nameEn: String?,
    val posterUrl: String?,
    val sex: String? = null
)

