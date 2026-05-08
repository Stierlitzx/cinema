package kz.stierlitz.skillcinema.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import kz.stierlitz.skillcinema.data.local.dao.CollectionDao
import kz.stierlitz.skillcinema.data.local.dao.CollectionMovieDao
import kz.stierlitz.skillcinema.data.local.dao.WatchedMovieDao
import kz.stierlitz.skillcinema.data.local.entity.CollectionEntity
import kz.stierlitz.skillcinema.data.local.entity.CollectionMovieEntity
import kz.stierlitz.skillcinema.data.local.entity.WatchedMovieEntity

@Database(entities = [WatchedMovieEntity::class, CollectionEntity::class, CollectionMovieEntity::class], version = 3, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract val watchedMovieDao: WatchedMovieDao
    abstract val collectionDao: CollectionDao
    abstract val collectionMovieDao: CollectionMovieDao
}
