package com.example.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class ReminderReceiver : BroadcastReceiver() {

    companion object {
        const val ACTION_MORNING_REMINDER = "com.example.adhkari.ACTION_MORNING"
        const val ACTION_EVENING_REMINDER = "com.example.adhkari.ACTION_EVENING"
    }

    override fun onReceive(context: Context, intent: Intent?) {
        when (intent?.action) {
            ACTION_MORNING_REMINDER -> {
                NotificationHelper.showNotification(
                    context,
                    NotificationHelper.MORNING_NOTIFICATION_ID,
                    "أذكار الصباح ☀️",
                    "حان وقت أذكار الصباح، حصّن يومك بالذكر واكسب رضا الرحمن."
                )
            }
            ACTION_EVENING_REMINDER -> {
                NotificationHelper.showNotification(
                    context,
                    NotificationHelper.EVENING_NOTIFICATION_ID,
                    "أذكار المساء 🌙",
                    "حان وقت أذكار المساء، اختم يومك بذكر الله والأدعية المأثورة."
                )
            }
        }
    }
}
