package com.example.data.local

import com.example.data.datasource.AuthenticAzkarProvider
import com.example.data.local.entity.DhikrProgressEntity
import com.example.data.local.entity.FavoriteEntity
import com.example.data.local.entity.ReadingHistoryEntity
import com.example.data.local.entity.TasbihRecordEntity
import com.example.data.model.AzkarCategory
import com.example.data.model.DhikrItem
import com.example.data.model.TasbihPreset
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DhikrRepository(private val dhikrDao: DhikrDao) {

    // --- Core Azkar Data ---
    fun getAzkarByCategory(category: AzkarCategory): List<DhikrItem> {
        return AuthenticAzkarProvider.getAzkarByCategory(category)
    }

    fun getDhikrById(id: String): DhikrItem? {
        return AuthenticAzkarProvider.getDhikrById(id)
    }

    fun searchAzkar(query: String): List<DhikrItem> {
        return AuthenticAzkarProvider.searchAzkar(query)
    }

    fun getTasbihPresets(): List<TasbihPreset> {
        return AuthenticAzkarProvider.tasbihPresets
    }

    // --- Favorites ---
    val favoriteIds: Flow<List<String>> = dhikrDao.getAllFavoriteIds()

    val favoriteDhikrs: Flow<List<DhikrItem>> = dhikrDao.getAllFavoriteIds().map { ids ->
        ids.mapNotNull { AuthenticAzkarProvider.getDhikrById(it) }
    }

    suspend fun toggleFavorite(dhikrId: String, isCurrentlyFavorite: Boolean) {
        if (isCurrentlyFavorite) {
            dhikrDao.removeFavorite(dhikrId)
        } else {
            dhikrDao.addFavorite(FavoriteEntity(dhikrId))
        }
    }

    fun isFavorite(dhikrId: String): Flow<Boolean> = dhikrDao.isFavorite(dhikrId)

    // --- Progress Tracking ---
    val allProgress: Flow<Map<String, DhikrProgressEntity>> = dhikrDao.getAllProgress().map { list ->
        list.associateBy { it.dhikrId }
    }

    suspend fun incrementProgress(dhikrId: String, currentCount: Int, targetCount: Int): Int {
        val nextCount = (currentCount + 1).coerceAtMost(targetCount)
        dhikrDao.saveProgress(
            DhikrProgressEntity(
                dhikrId = dhikrId,
                completedCount = nextCount,
                isFullyCompleted = nextCount >= targetCount
            )
        )
        return nextCount
    }

    suspend fun resetProgress(dhikrId: String) {
        dhikrDao.resetDhikrProgress(dhikrId)
    }

    suspend fun resetAllProgress() {
        dhikrDao.resetAllProgress()
    }

    // --- Last Read History ---
    val lastRead: Flow<ReadingHistoryEntity?> = dhikrDao.getLastRead()

    suspend fun saveLastRead(dhikr: DhikrItem) {
        dhikrDao.updateLastRead(
            ReadingHistoryEntity(
                id = 1,
                dhikrId = dhikr.id,
                categoryId = dhikr.category.id,
                categoryTitle = dhikr.category.titleArabic,
                snippet = dhikr.textArabic.take(60) + if (dhikr.textArabic.length > 60) "..." else ""
            )
        )
    }

    // --- Tasbih ---
    val tasbihRecords: Flow<Map<String, TasbihRecordEntity>> = dhikrDao.getAllTasbihRecords().map { list ->
        list.associateBy { it.presetId }
    }

    fun getTasbihRecord(presetId: String): Flow<TasbihRecordEntity?> {
        return dhikrDao.getTasbihRecord(presetId)
    }

    suspend fun incrementTasbih(presetId: String, arabicText: String, currentCount: Int, targetCount: Int, totalLifetime: Int) {
        val nextCurrent = if (targetCount > 0 && currentCount >= targetCount) 1 else currentCount + 1
        val nextLifetime = totalLifetime + 1
        dhikrDao.saveTasbihRecord(
            TasbihRecordEntity(
                presetId = presetId,
                arabicText = arabicText,
                currentCount = nextCurrent,
                targetCount = targetCount,
                totalLifetimeCount = nextLifetime
            )
        )
    }

    suspend fun resetTasbih(presetId: String) {
        dhikrDao.resetTasbihCurrent(presetId)
    }
}
