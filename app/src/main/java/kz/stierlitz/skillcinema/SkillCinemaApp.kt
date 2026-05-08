package kz.stierlitz.skillcinema

import android.app.Application
import androidx.room.Room
import kz.stierlitz.skillcinema.data.local.AppDatabase

class SkillCinemaApp : Application() {

    lateinit var database: AppDatabase
        private set

    override fun onCreate() {
        super.onCreate()
        instance = this
        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "skillcinema_db"
        ).fallbackToDestructiveMigration().build()
    }

    companion object {
        lateinit var instance: SkillCinemaApp
            private set
    }
}
