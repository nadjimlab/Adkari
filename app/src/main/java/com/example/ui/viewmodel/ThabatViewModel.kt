package com.example.ui.viewmodel

import android.app.Application
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

private val Application.thabatDataStore by preferencesDataStore(name = "thabat_preferences")

data class PrayerState(val fajr: Boolean = false, val dhuhr: Boolean = false, val asr: Boolean = false, val maghrib: Boolean = false, val isha: Boolean = false) {
    val completed: Int get() = listOf(fajr, dhuhr, asr, maghrib, isha).count { it }
}

data class RecoveryState(val streak: Int = 0, val lifetimeDays: Int = 0, val lastRelapseTrigger: String? = null)

data class ThabatState(val prayers: PrayerState = PrayerState(), val recovery: RecoveryState = RecoveryState(), val journeyDay: Int = 1, val completedDays: Set<Int> = emptySet())

class ThabatViewModel(application: Application) : AndroidViewModel(application) {
    private val _state = MutableStateFlow(ThabatState())
    val state: StateFlow<ThabatState> = _state.asStateFlow()

    init { viewModelScope.launch { load() } }

    private suspend fun load() {
        val values = getApplication<Application>().thabatDataStore.data.first()
        val prayers = PrayerState(
            values[Key.FAJR] ?: false, values[Key.DHUHR] ?: false, values[Key.ASR] ?: false,
            values[Key.MAGHRIB] ?: false, values[Key.ISHA] ?: false
        )
        val done = (values[Key.DONE_DAYS] ?: "").split(",").mapNotNull { it.toIntOrNull() }.toSet()
        _state.value = ThabatState(prayers, RecoveryState(values[Key.STREAK] ?: 0, values[Key.LIFETIME] ?: 0, values[Key.TRIGGER]), values[Key.JOURNEY_DAY] ?: 1, done)
    }

    fun togglePrayer(index: Int) = viewModelScope.launch {
        val old = _state.value.prayers
        val next = when (index) { 0 -> old.copy(fajr = !old.fajr); 1 -> old.copy(dhuhr = !old.dhuhr); 2 -> old.copy(asr = !old.asr); 3 -> old.copy(maghrib = !old.maghrib); else -> old.copy(isha = !old.isha) }
        _state.value = _state.value.copy(prayers = next)
        getApplication<Application>().thabatDataStore.edit { p -> p[Key.prayer(index)] = listOf(next.fajr, next.dhuhr, next.asr, next.maghrib, next.isha)[index] }
    }

    fun completeToday() = viewModelScope.launch {
        val current = _state.value
        val done = current.completedDays + current.journeyDay
        val nextDay = (current.journeyDay + 1).coerceAtMost(30)
        _state.value = current.copy(journeyDay = nextDay, completedDays = done)
        getApplication<Application>().thabatDataStore.edit { p -> p[Key.DONE_DAYS] = done.joinToString(","); p[Key.JOURNEY_DAY] = nextDay }
    }

    fun recordRelapse(trigger: String) = viewModelScope.launch {
        _state.value = _state.value.copy(recovery = RecoveryState(0, _state.value.recovery.lifetimeDays, trigger))
        getApplication<Application>().thabatDataStore.edit { p -> p[Key.STREAK] = 0; p[Key.TRIGGER] = trigger }
    }

    fun markRecoveryDay() = viewModelScope.launch {
        val recovery = _state.value.recovery.copy(streak = _state.value.recovery.streak + 1, lifetimeDays = _state.value.recovery.lifetimeDays + 1)
        _state.value = _state.value.copy(recovery = recovery)
        getApplication<Application>().thabatDataStore.edit { p -> p[Key.STREAK] = recovery.streak; p[Key.LIFETIME] = recovery.lifetimeDays }
    }

    private object Key {
        val FAJR = booleanPreferencesKey("prayer_fajr"); val DHUHR = booleanPreferencesKey("prayer_dhuhr"); val ASR = booleanPreferencesKey("prayer_asr"); val MAGHRIB = booleanPreferencesKey("prayer_maghrib"); val ISHA = booleanPreferencesKey("prayer_isha")
        val STREAK = intPreferencesKey("recovery_streak"); val LIFETIME = intPreferencesKey("recovery_lifetime"); val JOURNEY_DAY = intPreferencesKey("journey_day")
        val DONE_DAYS = androidx.datastore.preferences.core.stringPreferencesKey("completed_days"); val TRIGGER = androidx.datastore.preferences.core.stringPreferencesKey("last_trigger")
        fun prayer(index: Int) = listOf(FAJR, DHUHR, ASR, MAGHRIB, ISHA)[index]
    }
}
