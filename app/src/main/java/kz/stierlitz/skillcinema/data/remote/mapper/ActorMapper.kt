package kz.stierlitz.skillcinema.data.remote.mapper

import kz.stierlitz.skillcinema.data.remote.dto.ActorDetailResponse
import kz.stierlitz.skillcinema.data.remote.dto.ActorFilmDto
import kz.stierlitz.skillcinema.domain.model.ActorDetail
import kz.stierlitz.skillcinema.domain.model.ActorFilm
import kz.stierlitz.skillcinema.domain.model.Movie

fun ActorDetailResponse.toDomain(): ActorDetail = ActorDetail(
    personId = personId,
    nameRu = nameRu,
    nameEn = nameEn,
    posterUrl = posterUrl,
    profession = profession,
    films = films.map { it.toDomain() }
)

fun ActorFilmDto.toDomain(): ActorFilm = ActorFilm(
    filmId = filmId,
    nameRu = nameRu,
    nameEn = nameEn,
    rating = rating,
    description = description,
    professionKey = professionKey
)

fun ActorFilm.toFilm(): Movie = Movie(
    kinopoiskId = filmId,
    nameRu = nameRu,
    nameEn = nameEn,
    posterUrl = "https://kinopoiskapiunofficial.tech/images/posters/kp/$filmId.jpg",
    posterUrlPreview = "https://kinopoiskapiunofficial.tech/images/posters/kp_small/$filmId.jpg",
    ratingKinopoisk = rating?.toDoubleOrNull(),
    year = null,
    genres = emptyList(),
    reviewsCount = 0,
    webUrl = "",
    isTicketsAvailable = false,
    type = "FILM",
    lastSync = ""
)
