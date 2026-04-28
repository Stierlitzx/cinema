package kz.stierlitz.skillcinema.data.remote.kinopoisk

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface KinopoiskApi {
    @GET("api/v2.2/films/collections")
    suspend fun getCollections(
        @Query("type") type: String = "TOP_POPULAR_ALL",
        @Query("page") page: Int = 1
    ): FilmCollectionResponse

    @GET("api/v2.2/films")
    suspend fun getFilmsByFilters(
        @Query("countries") countries: Int? = null,
        @Query("genres") genres: Int? = null,
        @Query("keyword") keyword: String? = null,
        @Query("page") page: Int = 1
    ): FilmSearchResponse

    @GET("api/v2.2/films/{id}")
    suspend fun getFilm(
        @Path("id") id: Int
    ): FilmResponse

    @GET("api/v1/staff")
    suspend fun getStaff(
        @Query("filmId") filmId: Int
    ): List<StaffResponse>

    @GET("api/v2.2/films/{id}/images")
    suspend fun getFilmImages(
        @Path("id") id: Int,
        @Query("type") type: String = "STILL",
        @Query("page") page: Int = 1
    ): FilmImageResponse

    @GET("api/v2.2/films/{id}/seasons")
    suspend fun getSeasons(
        @Path("id") id: Int
    ): SeasonsResponse
}
