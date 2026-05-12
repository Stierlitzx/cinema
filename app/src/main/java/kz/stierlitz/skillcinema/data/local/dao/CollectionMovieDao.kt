package kz.stierlitz.skillcinema.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import kz.stierlitz.skillcinema.data.local.entity.CollectionMovieEntity

@Dao
interface CollectionMovieDao {
    @Query("SELECT * FROM collection_movies WHERE collectionId = :collectionId")
    fun getMoviesInCollection(collectionId: Int): Flow<List<CollectionMovieEntity>>

    @Query("SELECT collectionId FROM collection_movies WHERE filmId = :filmId")
    fun getCollectionsForMovie(filmId: Int): Flow<List<Int>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovieToCollection(movie: CollectionMovieEntity)

    @Query("DELETE FROM collection_movies WHERE collectionId = :collectionId AND filmId = :filmId")
    suspend fun removeMovieFromCollection(collectionId: Int, filmId: Int)

    @Query("DELETE FROM collection_movies WHERE collectionId = :collectionId")
    suspend fun removeAllFromCollection(collectionId: Int)

    @Query("SELECT COUNT(*) FROM collection_movies WHERE collectionId = :collectionId")
    fun getMovieCountInCollection(collectionId: Int): Flow<Int>

    @Query("SELECT COUNT(*) FROM collection_movies WHERE collectionId = :collectionId")
    suspend fun getMovieCountSync(collectionId: Int): Int
}