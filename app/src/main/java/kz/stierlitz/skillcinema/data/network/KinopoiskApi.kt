package kz.stierlitz.skillcinema.data.network

import kz.stierlitz.skillcinema.domain.model.Movie
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

interface KinopoiskApi {

    @Headers(
        "X-API-KEY: 5bd518f3-e3c9-4d55-990c-804bb6c1fdb0", //5bd518f3-e3c9-4d55-990c-804bb6c1fdb0, 2e237281-ef26-4c9d-9a04-8fcf437013f4
        "Content-Type: application/json"
    )
    @GET("api/v2.2/films/{id}")
    suspend fun getMovieById(
        @Path("id") id: Int
    ): Movie

}