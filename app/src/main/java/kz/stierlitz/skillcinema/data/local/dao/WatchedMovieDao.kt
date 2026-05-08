package kz.stierlitz.skillcinema.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import kz.stierlitz.skillcinema.data.local.entity.WatchedMovieEntity

@Dao
interface WatchedMovieDao {

    @Query("SELECT * FROM watched_movies ORDER BY timestamp DESC")
    fun getWatchedMovies(): Flow<List<WatchedMovieEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWatchedMovie(movie: WatchedMovieEntity)

    @Query("DELETE FROM watched_movies")
    suspend fun clearWatchedHistory()
}

