package kz.stierlitz.skillcinema.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Movie(
    @SerialName("kinopoiskId")
    val kinopoiskId: Int,
    @SerialName("kinopoiskHDId")
    val kinopoiskHDId: String? = null,
    @SerialName("imdbId")
    val imdbId: String? = null,
    @SerialName("nameRu")
    val nameRu: String? = null,
    @SerialName("nameEn")
    val nameEn: String? = null,
    @SerialName("nameOriginal")
    val nameOriginal: String? = null,
    @SerialName("posterUrl")
    val posterUrl: String,
    @SerialName("posterUrlPreview")
    val posterUrlPreview: String,
    @SerialName("coverUrl")
    val coverUrl: String? = null,
    @SerialName("logoUrl")
    val logoUrl: String? = null,
    @SerialName("reviewsCount")
    val reviewsCount: Int,
    @SerialName("ratingGoodReview")
    val ratingGoodReview: Double? = null,
    @SerialName("ratingGoodReviewVoteCount")
    val ratingGoodReviewVoteCount: Int? = null,
    @SerialName("ratingKinopoisk")
    val ratingKinopoisk: Double? = null,
    @SerialName("ratingKinopoiskVoteCount")
    val ratingKinopoiskVoteCount: Int? = null,
    @SerialName("ratingImdb")
    val ratingImdb: Double? = null,
    @SerialName("ratingImdbVoteCount")
    val ratingImdbVoteCount: Int? = null,
    @SerialName("ratingFilmCritics")
    val ratingFilmCritics: Double? = null,
    @SerialName("ratingFilmCriticsVoteCount")
    val ratingFilmCriticsVoteCount: Int? = null,
    @SerialName("ratingAwait")
    val ratingAwait: Double? = null,
    @SerialName("ratingAwaitCount")
    val ratingAwaitCount: Int? = null,
    @SerialName("ratingRfCritics")
    val ratingRfCritics: Double? = null,
    @SerialName("ratingRfCriticsVoteCount")
    val ratingRfCriticsVoteCount: Int? = null,
    @SerialName("webUrl")
    val webUrl: String,
    @SerialName("year")
    val year: Int? = null,
    @SerialName("filmLength")
    val filmLength: Int? = null,
    @SerialName("slogan")
    val slogan: String? = null,
    @SerialName("description")
    val description: String? = null,
    @SerialName("shortDescription")
    val shortDescription: String? = null,
    @SerialName("editorAnnotation")
    val editorAnnotation: String? = null,
    @SerialName("isTicketsAvailable")
    val isTicketsAvailable: Boolean,
    @SerialName("productionStatus")
    val productionStatus: String? = null,
    @SerialName("type")
    val type: String,
    @SerialName("ratingMpaa")
    val ratingMpaa: String? = null,
    @SerialName("ratingAgeLimits")
    val ratingAgeLimits: String? = null,
    @SerialName("hasImax")
    val hasImax: Boolean? = null,
    @SerialName("has3D")
    val has3D: Boolean? = null,
    @SerialName("lastSync")
    val lastSync: String,
    @SerialName("startYear")
    val startYear: Int? = null,
    @SerialName("endYear")
    val endYear: Int? = null,
    @SerialName("serial")
    val serial: Boolean? = null,
    @SerialName("shortFilm")
    val shortFilm: Boolean? = null,
    @SerialName("completed")
    val completed: Boolean? = null,

    @SerialName("countries")
    val countries: List<Country> = emptyList(),
    @SerialName("genres")
    val genres: List<Genre> = emptyList()
)

@Serializable
data class Country(
    @SerialName("country")
    val name: String
)

@Serializable
data class Genre(
    @SerialName("genre")
    val name: String
)
