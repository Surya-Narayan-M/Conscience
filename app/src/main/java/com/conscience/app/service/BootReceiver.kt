package com.conscience.app.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.conscience.app.notifications.NotificationScheduler

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            // Restart foreground service (accessibility service auto-starts if enabled in Settings)
            val serviceIntent = Intent(context, ConscienceForegroundService::class.java)
            context.startForegroundService(serviceIntent)

            // Reschedule daily summary alarm (alarms are cleared on reboot)
            NotificationScheduler.scheduleDailySummary(context)
        }
    }
}
