package com.conscience.app.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.conscience.app.data.prefs.AppPreferences
import com.conscience.app.utils.Constants

class DailySummaryReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val prefs = AppPreferences(context)
        val answers = prefs.getTodayAnswers()
        if (answers.isEmpty()) {
            NotificationScheduler.scheduleDailySummary(context)
            return
        }

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        ensureChannel(notificationManager)

        val inbox = NotificationCompat.InboxStyle()
        answers.forEach { (question, answer) ->
            inbox.addLine("Q: $question | A: $answer")
        }

        val notification = NotificationCompat.Builder(context, Constants.DAILY_SUMMARY_CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Conscience Daily Summary")
            .setContentText("Questions answered today: ${answers.size}")
            .setStyle(inbox)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(Constants.DAILY_SUMMARY_NOTIFICATION_ID, notification)
        prefs.clearTodayAnswers()
        NotificationScheduler.scheduleDailySummary(context)
    }

    private fun ensureChannel(notificationManager: NotificationManager) {
        val channel = NotificationChannel(
            Constants.DAILY_SUMMARY_CHANNEL_ID,
            "Daily Summary",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        notificationManager.createNotificationChannel(channel)
    }
}
