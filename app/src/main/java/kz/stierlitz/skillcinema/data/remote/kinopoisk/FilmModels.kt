package kz.stierlitz.skillcinema.data.remote.kinopoisk

import kotlinx.serialization.Serializable

@Serializable
data class FilmCollectionResponse(
    val total: Int = 0,
    val totalPages: Int = 0,
    val items: List<FilmCollectionItem> = emptyList()
)
@Serializable
data class FilmCollectionItem(
    val kinopoiskId: Int,
    val nameRu: String? = null,
    val nameEn: String? = null,
    val nameOriginal: String? = null,
    val posterUrl: String? = null,
    val posterUrlPreview: String? = null,
    val ratingKinopoisk: Double? = null,
    val ratingImbd: Double? = null,
    val year: Int? = null,
    val genres: List<Genre> = emptyList()
)
@Serializable
data class FilmSearchResponse(
    val total: Int = 0,
    val totalPages: Int = 0,
    val items: List<FilmSearchItem> = emptyList()
)
@Serializable
data class FilmSearchItem(
    val kinopoiskId: Int,
    val nameRu: String? = null,
    val nameEn: String? = null,
    val nameOriginal: String? = null,
    val posterUrl: String? = null,
    val posterUrlPreview: String? = null,
    val ratingKinopoisk: Double? = null,
    val ratingImdb: Double? = null,
    val year: Int? = null,
    val genres: List<Genre> = emptyList()
)
@Serializable
data class Genre(
    val genre: String? = null
)
@Serializable
data class Country(
    val country: String? = null
)
@Serializable
data class FilmResponse(
    val kinopoiskId: Int,
    val nameRu: String? = null,
    val nameEn: String? = null,
    val nameOriginal: String? = null,
    val posterUrl: String? = null,
    val posterUrlPreview: String? = null,
    val coverUrl: String? = null,
    val logoUrl: String? = null,
    val reviewsCount: Int = 0,
    val ratingGoodReview: Double? = null,
    val ratingKinopoisk: Double? = null,
    val ratingImdb: Double? = null,
    val webUrl: String? = null,
    val year: Int? = null,
    val filmLength: Int? = null,
    val slogan: String? = null,
    val description: String? = null,
    val shortDescription: String? = null,
    val type: String? = null,
    val ratingMpaa: String? = null,
    val ratingAgeLimits: String? = null,
    val countries: List<Country> = emptyList(),
    val genres: List<Genre> = emptyList()
)
@Serializable
data class StaffResponse(
    val staffId: Int,
    val nameRu: String? = null,
    val nameEn: String? = null,
    val description: String? = null,
    val posterUrl: String? = null,
    val professionText: String? = null,
    val professionKey: String? = null
)
@Serializable
data class FilmImageResponse(
    val total: Int = 0,
    val totalPages: Int = 0,
    val items: List<FilmImageItem> = emptyList()
)
@Serializable
data class FilmImageItem(
    val imageUrl: String,
    val previewUrl: String
)

@Serializable
data class SeasonsResponse(
    val total: Int = 0,
    val items: List<SeasonItem> = emptyList()
)

@Serializable
data class SeasonItem(
    val number: Int,
    val episodes: List<EpisodeItem> = emptyList()
)

@Serializable
data class EpisodeItem(
    val seasonNumber: Int,
    val episodeNumber: Int,
    val nameRu: String? = null,
    val nameEn: String? = null,
    val synopsis: String? = null,
    val releaseDate: String? = null
)
