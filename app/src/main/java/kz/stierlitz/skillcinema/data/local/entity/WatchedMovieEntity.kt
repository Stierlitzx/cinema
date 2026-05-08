package kz.stierlitz.skillcinema.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "watched_movies")
data class WatchedMovieEntity(
    @PrimaryKey
    val filmId: Int,
    val nameRu: String?,
    val nameEn: String?,
    val posterUrl: String?,
    val posterUrlPreview: String?,
    val ratingKinopoisk: Double?,
    val ratingImdb: Double?,
    val year: Int?,
    val timestamp: Long = System.currentTimeMillis()
)

