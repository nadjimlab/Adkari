package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.local.entity.DhikrProgressEntity
import com.example.data.local.entity.FavoriteEntity
import com.example.data.local.entity.ReadingHistoryEntity
import com.example.data.local.entity.TasbihRecordEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DhikrDao {

    // --- Favorites ---
    @Query("SELECT dhikrId FROM favorites ORDER BY timestamp DESC")
    fun getAllFavoriteIds(): Flow<List<String>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavorite(favorite: FavoriteEntity)

    @Query("DELETE FROM favorites WHERE dhikrId = :dhikrId")
    suspend fun removeFavorite(dhikrId: String)

    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE dhikrId = :dhikrId)")
    fun isFavorite(dhikrId: String): Flow<Boolean>

    // --- Progress ---
    @Query("SELECT * FROM dhikr_progress")
    fun getAllProgress(): Flow<List<DhikrProgressEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveProgress(progress: DhikrProgressEntity)

    @Query("DELETE FROM dhikr_progress WHERE dhikrId = :dhikrId")
    suspend fun resetDhikrProgress(dhikrId: String)

    @Query("DELETE FROM dhikr_progress")
    suspend fun resetAllProgress()

    // --- Reading History ---
    @Query("SELECT * FROM reading_history WHERE id = 1 LIMIT 1")
    fun getLastRead(): Flow<ReadingHistoryEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateLastRead(history: ReadingHistoryEntity)

    // --- Tasbih ---
    @Query("SELECT * FROM tasbih_records")
    fun getAllTasbihRecords(): Flow<List<TasbihRecordEntity>>

    @Query("SELECT * FROM tasbih_records WHERE presetId = :presetId LIMIT 1")
    fun getTasbihRecord(presetId: String): Flow<TasbihRecordEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveTasbihRecord(record: TasbihRecordEntity)

    @Query("UPDATE tasbih_records SET currentCount = 0 WHERE presetId = :presetId")
    suspend fun resetTasbihCurrent(presetId: String)
}
