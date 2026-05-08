package kz.stierlitz.skillcinema.data.repository

import kz.stierlitz.skillcinema.data.remote.kinopoisk.KinopoiskApi
import kz.stierlitz.skillcinema.domain.model.*
import kz.stierlitz.skillcinema.domain.repository.MovieRepository

class MovieRepositoryImpl(
    private val api: KinopoiskApi
) : MovieRepository {
    override suspend fun getCollections(type: String): List<Movie> {
        val response = api.getCollections(type = type)
        return response.items.map { item ->
            Movie(
                kinopoiskId = item.kinopoiskId,
                nameRu = item.nameRu,
                nameEn = item.nameEn,
                nameOriginal = item.nameOriginal,
                posterUrl = item.posterUrl ?: "",
                posterUrlPreview = item.posterUrlPreview ?: "",
                ratingKinopoisk = item.ratingKinopoisk,
                ratingImdb = item.ratingImbd,
                year = item.year ?: 0,
                genres = item.genres.map { Genre(it.genre ?: "") },
                reviewsCount = 0,
                webUrl = "",
                isTicketsAvailable = false,
                type = "FILM",
                lastSync = ""
            )
        }
    }

    override suspend fun getFilmsByFilters(
        countries: Int?,
        genres: Int?,
        keyword: String?,
        ratingFrom: Float?,
        ratingTo: Float?,
        yearFrom: Int?,
        yearTo: Int?
    ): List<Movie> {
        val response = api.getFilmsByFilters(
            countries = countries,
            genres = genres,
            keyword = keyword,
            ratingFrom = ratingFrom,
            ratingTo = ratingTo,
            yearFrom = yearFrom,
            yearTo = yearTo
        )
        return response.items.map { item ->
            Movie(
                kinopoiskId = item.kinopoiskId,
                nameRu = item.nameRu,
                nameEn = item.nameEn,
                nameOriginal = item.nameOriginal,
                posterUrl = item.posterUrl ?: "",
                posterUrlPreview = item.posterUrlPreview ?: "",
                ratingKinopoisk = item.ratingKinopoisk,
                ratingImdb = item.ratingImdb,
                year = item.year ?: 0,
                genres = item.genres.map { Genre(it.genre ?: "") },
                reviewsCount = 0,
                webUrl = "",
                isTicketsAvailable = false,
                type = "FILM",
                lastSync = ""
            )
        }
    }

    override suspend fun getFilm(id: Int): Movie {
        val f = api.getFilm(id)
        return Movie(
            kinopoiskId = f.kinopoiskId,
            nameRu = f.nameRu,
            nameEn = f.nameEn,
            nameOriginal = f.nameOriginal,
            posterUrl = f.posterUrl ?: "",
            posterUrlPreview = f.posterUrlPreview ?: "",
            coverUrl = f.coverUrl,
            logoUrl = f.logoUrl,
            ratingKinopoisk = f.ratingKinopoisk,
            ratingImdb = f.ratingImdb,
            year = f.year,
            filmLength = f.filmLength,
            slogan = f.slogan,
            description = f.description,
            shortDescription = f.shortDescription,
            ratingMpaa = f.ratingMpaa,
            ratingAgeLimits = f.ratingAgeLimits,
            countries = f.countries.map { Country(it.country ?: "") },
            genres = f.genres.map { Genre(it.genre ?: "") },
            reviewsCount = f.reviewsCount,
            webUrl = f.webUrl ?: "",
            isTicketsAvailable = false,
            type = f.type ?: "FILM",
            lastSync = ""
        )
    }

    override suspend fun getStaff(filmId: Int): List<Staff> {
        val response = api.getStaff(filmId)
        return response.map { s ->
            Staff(
                staffId = s.staffId,
                nameRu = s.nameRu,
                nameEn = s.nameEn,
                description = s.description,
                posterUrl = s.posterUrl,
                professionText = s.professionText,
                professionKey = s.professionKey
            )
        }
    }

    override suspend fun getFilmImages(id: Int, type: String, page: Int): List<FilmImage> {
        val response = api.getFilmImages(id, type, page)
        return response.items.map { i ->
            FilmImage(
                imageUrl = i.imageUrl,
                previewUrl = i.previewUrl
            )
        }
    }

    override suspend fun getSeasons(id: Int): List<Season> {
        val response = api.getSeasons(id)
        return response.items.map { season ->
            Season(
                number = season.number,
                episodes = season.episodes.map { ep ->
                    Episode(
                        seasonNumber = ep.seasonNumber,
                        episodeNumber = ep.episodeNumber,
                        nameRu = ep.nameRu,
                        nameEn = ep.nameEn,
                        synopsis = ep.synopsis,
                        releaseDate = ep.releaseDate
                    )
                }
            )
        }
    }
}