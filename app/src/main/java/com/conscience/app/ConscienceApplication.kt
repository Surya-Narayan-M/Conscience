package com.conscience.app

import android.app.Application
import com.conscience.app.notifications.NotificationScheduler
import com.conscience.app.security.DataSecurityManager

class ConscienceApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        DataSecurityManager.initialize(this)
        NotificationScheduler.scheduleDailySummary(this)
    }
}
