package com.example.ui.viewmodel

import android.app.Application
import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.local.DhikrRepository
import com.example.data.local.entity.DhikrProgressEntity
import com.example.data.local.entity.ReadingHistoryEntity
import com.example.data.local.entity.TasbihRecordEntity
import com.example.data.model.AzkarCategory
import com.example.data.model.DhikrItem
import com.example.data.model.TasbihPreset
import com.example.receiver.NotificationHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class AppSettingsState(
    val fontScale: Float = 1.0f,
    val hapticFeedbackEnabled: Boolean = true,
    val soundFeedbackEnabled: Boolean = false,
    val autoAdvanceEnabled: Boolean = true,
    val morningReminderEnabled: Boolean = false,
    val morningHour: Int = 6,
    val morningMinute: Int = 30,
    val eveningReminderEnabled: Boolean = false,
    val eveningHour: Int = 17,
    val eveningMinute: Int = 30,
    val isDarkMode: Boolean? = null // null means system default
)

class AdhkariViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: DhikrRepository

    init {
        val db = AppDatabase.getInstance(application)
        repository = DhikrRepository(db.dhikrDao())
        NotificationHelper.createNotificationChannel(application)
    }

    // --- Settings State ---
    private val _settings = MutableStateFlow(AppSettingsState())
    val settings: StateFlow<AppSettingsState> = _settings.asStateFlow()

    // --- Search Query State ---
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _searchResults = MutableStateFlow<List<DhikrItem>>(emptyList())
    val searchResults: StateFlow<List<DhikrItem>> = _searchResults.asStateFlow()

    // --- Active Reader State ---
    private val _activeCategory = MutableStateFlow(AzkarCategory.MORNING)
    val activeCategory: StateFlow<AzkarCategory> = _activeCategory.asStateFlow()

    private val _activeCategoryItems = MutableStateFlow<List<DhikrItem>>(emptyList())
    val activeCategoryItems: StateFlow<List<DhikrItem>> = _activeCategoryItems.asStateFlow()

    private val _currentDhikrIndex = MutableStateFlow(0)
    val currentDhikrIndex: StateFlow<Int> = _currentDhikrIndex.asStateFlow()

    // --- Room Data Flows ---
    val favoriteIds: StateFlow<List<String>> = repository.favoriteIds
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val favoriteDhikrs: StateFlow<List<DhikrItem>> = repository.favoriteDhikrs
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val progressMap: StateFlow<Map<String, DhikrProgressEntity>> = repository.allProgress
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyMap())

    val lastRead: StateFlow<ReadingHistoryEntity?> = repository.lastRead
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val tasbihRecords: StateFlow<Map<String, TasbihRecordEntity>> = repository.tasbihRecords
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyMap())

    // --- Active Tasbih Preset State ---
    val tasbihPresets: List<TasbihPreset> = repository.getTasbihPresets()
    private val _selectedTasbihPreset = MutableStateFlow(tasbihPresets.first())
    val selectedTasbihPreset: StateFlow<TasbihPreset> = _selectedTasbihPreset.asStateFlow()

    // Computed Stats
    val totalRepetitionsCompleted: StateFlow<Int> = progressMap.combine(tasbihRecords) { prog, tasb ->
        val azkarCount = prog.values.sumOf { it.completedCount }
        val tasbihCount = tasb.values.sumOf { it.totalLifetimeCount }
        azkarCount + tasbihCount
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    fun selectCategory(category: AzkarCategory) {
        _activeCategory.value = category
        val items = repository.getAzkarByCategory(category)
        _activeCategoryItems.value = items
        _currentDhikrIndex.value = 0
    }

    fun openDhikrReader(category: AzkarCategory, dhikrIndex: Int = 0) {
        _activeCategory.value = category
        val items = repository.getAzkarByCategory(category)
        _activeCategoryItems.value = items
        _currentDhikrIndex.value = dhikrIndex.coerceIn(0, (items.size - 1).coerceAtLeast(0))

        if (items.isNotEmpty()) {
            val dhikr = items[_currentDhikrIndex.value]
            viewModelScope.launch {
                repository.saveLastRead(dhikr)
            }
        }
    }

    fun nextDhikr(): Boolean {
        val items = _activeCategoryItems.value
        if (_currentDhikrIndex.value < items.size - 1) {
            _currentDhikrIndex.value += 1
            viewModelScope.launch {
                repository.saveLastRead(items[_currentDhikrIndex.value])
            }
            return true
        }
        return false
    }

    fun previousDhikr(): Boolean {
        val items = _activeCategoryItems.value
        if (_currentDhikrIndex.value > 0) {
            _currentDhikrIndex.value -= 1
            viewModelScope.launch {
                repository.saveLastRead(items[_currentDhikrIndex.value])
            }
            return true
        }
        return false
    }

    fun incrementDhikrCounter(dhikr: DhikrItem): Boolean {
        performHapticFeedback()
        val currentProg = progressMap.value[dhikr.id]?.completedCount ?: 0
        viewModelScope.launch {
            val newCount = repository.incrementProgress(dhikr.id, currentProg, dhikr.repeatCount)
            if (newCount >= dhikr.repeatCount && _settings.value.autoAdvanceEnabled) {
                // Short delay or immediate advance
                nextDhikr()
            }
        }
        return (currentProg + 1) >= dhikr.repeatCount
    }

    fun resetDhikrCounter(dhikrId: String) {
        viewModelScope.launch {
            repository.resetProgress(dhikrId)
        }
    }

    fun resetAllCategoryProgress(category: AzkarCategory) {
        val items = repository.getAzkarByCategory(category)
        viewModelScope.launch {
            items.forEach { repository.resetProgress(it.id) }
        }
    }

    fun toggleFavorite(dhikrId: String) {
        val isFav = favoriteIds.value.contains(dhikrId)
        viewModelScope.launch {
            repository.toggleFavorite(dhikrId, isFav)
        }
    }

    // --- Search ---
    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
        _searchResults.value = repository.searchAzkar(query)
    }

    // --- Tasbih Actions ---
    fun selectTasbihPreset(preset: TasbihPreset) {
        _selectedTasbihPreset.value = preset
    }

    fun incrementTasbih() {
        performHapticFeedback()
        val preset = _selectedTasbihPreset.value
        val record = tasbihRecords.value[preset.id]
        val current = record?.currentCount ?: 0
        val lifetime = record?.totalLifetimeCount ?: 0
        val target = record?.targetCount ?: preset.targetCount

        viewModelScope.launch {
            repository.incrementTasbih(
                presetId = preset.id,
                arabicText = preset.textArabic,
                currentCount = current,
                targetCount = target,
                totalLifetime = lifetime
            )
        }
    }

    fun resetTasbihCurrent() {
        val preset = _selectedTasbihPreset.value
        viewModelScope.launch {
            repository.resetTasbih(preset.id)
        }
    }

    // --- Settings Actions ---
    fun setFontScale(scale: Float) {
        _settings.value = _settings.value.copy(fontScale = scale.coerceIn(0.75f, 1.4f))
    }

    fun toggleHapticFeedback(enabled: Boolean) {
        _settings.value = _settings.value.copy(hapticFeedbackEnabled = enabled)
    }

    fun toggleAutoAdvance(enabled: Boolean) {
        _settings.value = _settings.value.copy(autoAdvanceEnabled = enabled)
    }

    fun toggleDarkMode(isDark: Boolean?) {
        _settings.value = _settings.value.copy(isDarkMode = isDark)
    }

    fun updateMorningReminder(enabled: Boolean, hour: Int = 6, minute: Int = 30) {
        _settings.value = _settings.value.copy(
            morningReminderEnabled = enabled,
            morningHour = hour,
            morningMinute = minute
        )
        NotificationHelper.scheduleDailyReminder(
            getApplication(),
            isMorning = true,
            hour = hour,
            minute = minute,
            enabled = enabled
        )
    }

    fun updateEveningReminder(enabled: Boolean, hour: Int = 17, minute: Int = 30) {
        _settings.value = _settings.value.copy(
            eveningReminderEnabled = enabled,
            eveningHour = hour,
            eveningMinute = minute
        )
        NotificationHelper.scheduleDailyReminder(
            getApplication(),
            isMorning = false,
            hour = hour,
            minute = minute,
            enabled = enabled
        )
    }

    private fun performHapticFeedback() {
        if (!_settings.value.hapticFeedbackEnabled) return
        try {
            val context = getApplication<Application>()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
                val vibrator = vibratorManager?.defaultVibrator
                vibrator?.vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_CLICK))
            } else {
                @Suppress("DEPRECATION")
                val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
                @Suppress("DEPRECATION")
                vibrator?.vibrate(30)
            }
        } catch (e: Exception) {
            // Ignore if vibration fails
        }
    }
}
