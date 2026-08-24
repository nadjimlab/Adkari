package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class FavoriteEntity(
    @PrimaryKey val dhikrId: String,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "dhikr_progress")
data class DhikrProgressEntity(
    @PrimaryKey val dhikrId: String,
    val completedCount: Int,
    val isFullyCompleted: Boolean = false,
    val lastUpdated: Long = System.currentTimeMillis()
)

@Entity(tableName = "reading_history")
data class ReadingHistoryEntity(
    @PrimaryKey val id: Int = 1, // Single record for last read
    val dhikrId: String,
    val categoryId: String,
    val categoryTitle: String,
    val snippet: String,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "tasbih_records")
data class TasbihRecordEntity(
    @PrimaryKey val presetId: String,
    val arabicText: String,
    val currentCount: Int = 0,
    val targetCount: Int = 33,
    val totalLifetimeCount: Int = 0,
    val lastUpdated: Long = System.currentTimeMillis()
)
