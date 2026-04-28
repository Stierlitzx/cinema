package kz.stierlitz.skillcinema.domain.repository

import kz.stierlitz.skillcinema.domain.model.FilmImage
import kz.stierlitz.skillcinema.domain.model.Movie
import kz.stierlitz.skillcinema.domain.model.Staff
import kz.stierlitz.skillcinema.domain.model.Season

interface MovieRepository {
    suspend fun getCollections(type: String): List<Movie>
    suspend fun getFilmsByFilters(countries: Int? = null, genres: Int? = null, keyword: String? = null): List<Movie>
    suspend fun getFilm(id: Int): Movie
    suspend fun getStaff(filmId: Int): List<Staff>
    suspend fun getFilmImages(id: Int): List<FilmImage>
    suspend fun getSeasons(id: Int): List<Season>
}
