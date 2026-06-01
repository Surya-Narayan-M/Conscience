package com.conscience.app.utils

object Constants {
    // Package to monitor
    const val INSTAGRAM_PACKAGE = "com.instagram.android"

    // Overlay timing
    const val ENTRY_INTERVENTION_DURATION_MS = 20_000L
    const val BONUS_PUNISHMENT_DURATION_MS = 5_000L

    // Escalation thresholds (in milliseconds)
    val ESCALATION_THRESHOLDS = listOf(
        0L to 10 * 60 * 1000L,        // 0–10 min: 10 min interval
        10 * 60 * 1000L to 7 * 60 * 1000L,   // 10–20 min: 7 min interval
        20 * 60 * 1000L to 5 * 60 * 1000L,   // 20–30 min: 5 min interval
        30 * 60 * 1000L to 3 * 60 * 1000L,   // 30–45 min: 3 min interval
        45 * 60 * 1000L to 2 * 60 * 1000L,   // 45–60 min: 2 min interval
        60 * 60 * 1000L to 1 * 60 * 1000L    // 60 min+: 1 min interval
    )

    // Daily usage escalation thresholds (total daily Instagram time)
    val DAILY_ESCALATION_THRESHOLDS = listOf(
        0L to 10 * 60 * 1000L,        // 0–30 min: 10 min interval
        30 * 60 * 1000L to 7 * 60 * 1000L,   // 30–60 min: 7 min interval
        60 * 60 * 1000L to 5 * 60 * 1000L,   // 60–75 min: 5 min interval
        75 * 60 * 1000L to 3 * 60 * 1000L,   // 75–90 min: 3 min interval
        90 * 60 * 1000L to 1 * 60 * 1000L    // 90 min+: 1 min interval
    )

    // Notification
    const val NOTIFICATION_CHANNEL_ID = "conscience_service_channel"
    const val NOTIFICATION_ID = 1001
    const val FOREGROUND_SERVICE_NOTIFICATION_ID = 1002
    const val DAILY_SUMMARY_CHANNEL_ID = "conscience_daily_summary_channel"
    const val DAILY_SUMMARY_NOTIFICATION_ID = 2001

    // Intent actions
    const val ACTION_SHOW_ENTRY_INTERVENTION = "com.conscience.app.ENTRY_INTERVENTION"
    const val ACTION_SHOW_REALITY_CHECK = "com.conscience.app.REALITY_CHECK"
    const val ACTION_INSTAGRAM_CLOSED = "com.conscience.app.INSTAGRAM_CLOSED"

    // SharedPreferences keys
    const val PREFS_NAME = "conscience_prefs"
    const val KEY_APP_ENABLED = "app_enabled"
    const val KEY_ONBOARDING_COMPLETE = "onboarding_complete"
    const val KEY_DAILY_OPEN_COUNT = "daily_open_count"
    const val KEY_DAILY_TIME_MS = "daily_time_ms"
    const val KEY_LAST_RESET_DATE = "last_reset_date"
    const val KEY_TOTAL_OPENS_ALLTIME = "total_opens_alltime"
    const val KEY_EFFECTIVE_DAILY_LIMIT_MIN = "effective_daily_limit_min"
    const val KEY_FREQUENCY_LEVEL = "frequency_level"
    const val KEY_DAILY_SUMMARY_TIME_MIN = "daily_summary_time_min"
    const val KEY_ANSWERS_DATE = "answers_date"
    const val KEY_TODAY_ANSWERS_JSON = "today_answers_json"

    // Database
    const val DATABASE_NAME = "conscience_db"
    const val DATABASE_VERSION = 1
}
