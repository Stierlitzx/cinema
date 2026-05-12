package kz.stierlitz.skillcinema.domain.repository

import kz.stierlitz.skillcinema.domain.model.FilmImage
import kz.stierlitz.skillcinema.domain.model.Movie
import kz.stierlitz.skillcinema.domain.model.Staff
import kz.stierlitz.skillcinema.domain.model.Season
import kz.stierlitz.skillcinema.domain.model.Person

interface MovieRepository {
    suspend fun getCollections(type: String): List<Movie>
    suspend fun getFilmsByFilters(
        countries: Int? = null,
        genres: Int? = null,
        keyword: String? = null,
        ratingFrom: Float? = null,
        ratingTo: Float? = null,
        yearFrom: Int? = null,
        yearTo: Int? = null
    ): List<Movie>
    suspend fun getFilm(id: Int): Movie
    suspend fun getStaff(filmId: Int): List<Staff>
    suspend fun getFilmImages(id: Int, type: String = "STILL", page: Int = 1): List<FilmImage>
    suspend fun getSeasons(id: Int): List<Season>
    suspend fun searchPersonsByName(name: String): List<Person>
    suspend fun getSimilarFilms(id: Int): List<Movie>
}
