package com.conscience.app.notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.conscience.app.data.prefs.AppPreferences
import java.util.Calendar

object NotificationScheduler {

    fun scheduleDailySummary(context: Context) {
        val prefs = AppPreferences(context)
        val minutes = prefs.dailySummaryTimeMinutes
        val hour = minutes / 60
        val minute = minutes % 60

        val cal = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        if (cal.timeInMillis <= System.currentTimeMillis()) {
            cal.add(Calendar.DAY_OF_YEAR, 1)
        }

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, DailySummaryReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val triggerAt = cal.timeInMillis
        if (canUseExactAlarms(alarmManager)) {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                triggerAt,
                pendingIntent
            )
        } else {
            // Use inexact scheduling to avoid exact-alarm permission crash.
            alarmManager.setWindow(
                AlarmManager.RTC_WAKEUP,
                triggerAt,
                INEXACT_WINDOW_MS,
                pendingIntent
            )
        }
    }

    private fun canUseExactAlarms(alarmManager: AlarmManager): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            alarmManager.canScheduleExactAlarms()
        } else {
            true
        }
    }

    private const val INEXACT_WINDOW_MS = 15 * 60 * 1000L
}
