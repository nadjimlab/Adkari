package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.local.entity.DhikrProgressEntity
import com.example.data.local.entity.FavoriteEntity
import com.example.data.local.entity.ReadingHistoryEntity
import com.example.data.local.entity.TasbihRecordEntity

@Database(
    entities = [
        FavoriteEntity::class,
        DhikrProgressEntity::class,
        ReadingHistoryEntity::class,
        TasbihRecordEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun dhikrDao(): DhikrDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "adhkari_database.db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
