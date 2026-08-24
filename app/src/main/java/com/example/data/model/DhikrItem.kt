package com.example.data.model

data class DhikrItem(
    val id: String,
    val category: AzkarCategory,
    val textArabic: String,
    val repeatCount: Int = 1,
    val reference: String,
    val virtue: String = "", // فضل الذكر
    val notes: String = "",
    val isQuranic: Boolean = false
)

data class TasbihPreset(
    val id: String,
    val textArabic: String,
    val targetCount: Int = 33,
    val virtue: String = ""
)
