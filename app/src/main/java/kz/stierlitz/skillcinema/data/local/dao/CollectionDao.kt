package kz.stierlitz.skillcinema.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import kz.stierlitz.skillcinema.data.local.entity.CollectionEntity

@Dao
interface CollectionDao {
    @Query("SELECT * FROM movie_collections")
    fun getAllCollections(): Flow<List<CollectionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCollection(collection: CollectionEntity): Long

    @Query("DELETE FROM movie_collections WHERE id = :id AND isCustom = 1")
    suspend fun deleteCustomCollection(id: Int)

    @Query("SELECT COUNT(*) FROM movie_collections")
    suspend fun getCollectionCount(): Int

    @Query("SELECT * FROM movie_collections WHERE name = :name LIMIT 1")
    suspend fun getCollectionByName(name: String): CollectionEntity?
}
