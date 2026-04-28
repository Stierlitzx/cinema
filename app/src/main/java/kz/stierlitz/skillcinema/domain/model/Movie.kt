package kz.stierlitz.skillcinema.domain.model





data class Movie(
    
    val kinopoiskId: Int,
    
    val kinopoiskHDId: String? = null,
    
    val imdbId: String? = null,
    
    val nameRu: String? = null,
    
    val nameEn: String? = null,
    
    val nameOriginal: String? = null,
    
    val posterUrl: String,
    
    val posterUrlPreview: String,
    
    val coverUrl: String? = null,
    
    val logoUrl: String? = null,
    
    val reviewsCount: Int,
    
    val ratingGoodReview: Double? = null,
    
    val ratingGoodReviewVoteCount: Int? = null,
    
    val ratingKinopoisk: Double? = null,
    
    val ratingKinopoiskVoteCount: Int? = null,
    
    val ratingImdb: Double? = null,
    
    val ratingImdbVoteCount: Int? = null,
    
    val ratingFilmCritics: Double? = null,
    
    val ratingFilmCriticsVoteCount: Int? = null,
    
    val ratingAwait: Double? = null,
    
    val ratingAwaitCount: Int? = null,
    
    val ratingRfCritics: Double? = null,
    
    val ratingRfCriticsVoteCount: Int? = null,
    
    val webUrl: String,
    
    val year: Int? = null,
    
    val filmLength: Int? = null,
    
    val slogan: String? = null,
    
    val description: String? = null,
    
    val shortDescription: String? = null,
    
    val editorAnnotation: String? = null,
    
    val isTicketsAvailable: Boolean,
    
    val productionStatus: String? = null,
    
    val type: String,
    
    val ratingMpaa: String? = null,
    
    val ratingAgeLimits: String? = null,
    
    val hasImax: Boolean? = null,
    
    val has3D: Boolean? = null,
    
    val lastSync: String,
    
    val startYear: Int? = null,
    
    val endYear: Int? = null,
    
    val serial: Boolean? = null,
    
    val shortFilm: Boolean? = null,
    
    val completed: Boolean? = null,

    
    val countries: List<Country> = emptyList(),
    
    val genres: List<Genre> = emptyList()
)


data class Country(
    
    val name: String
)


data class Genre(
    
    val name: String
)
