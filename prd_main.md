# CONSCIENCE APP — PRODUCT REQUIREMENTS DOCUMENT
### Version 1.0 | Target: AI Agent Implementation Guide
### Platform: Android (API 26+) | Language: Kotlin

---

> **AGENT INSTRUCTION**: This PRD is written for an AI agent to implement end-to-end with minimal ambiguity. Every section contains exact code, file paths, class names, and expected outputs. Do NOT deviate from naming conventions. Do NOT infer — use exact specifications. When in doubt, refer to the VERIFICATION GUIDE document before proceeding.

---

## TABLE OF CONTENTS

1. [Project Overview](#1-project-overview)
2. [Architecture Overview](#2-architecture-overview)
3. [Project Setup](#3-project-setup)
4. [Permissions & Manifest](#4-permissions--manifest)
5. [Data Layer](#5-data-layer)
6. [Accessibility Service](#6-accessibility-service)
7. [Session Manager](#7-session-manager)
8. [Overlay Engine](#8-overlay-engine)
9. [Entry Intervention Screen](#9-entry-intervention-screen)
10. [Continuous Reality Check Overlay](#10-continuous-reality-check-overlay)
11. [MCQ Question Bank](#11-mcq-question-bank)
12. [Stats Engine](#12-stats-engine)
13. [Settings Screen](#13-settings-screen)
14. [Main Activity & Onboarding](#14-main-activity--onboarding)
15. [Security Implementation](#15-security-implementation)
16. [Build & Gradle Configuration](#16-build--gradle-configuration)
17. [Phase Rollout Plan](#17-phase-rollout-plan)

---

## 1. PROJECT OVERVIEW

### App Name
`ConscienceApp` — Package: `com.conscience.app`

### Purpose
An Android application that intercepts Instagram usage and confronts the user with unskippable psychological reality-check messages and MCQ questions. Designed to build mindfulness around social media consumption by escalating friction the longer the user stays on Instagram.

### Core Behavior Summary

| Trigger | Action | Duration |
|---|---|---|
| Instagram opens | Fullscreen entry intervention | 20 seconds, unskippable |
| Every N minutes on Instagram | MCQ reality check overlay | Until answered |
| Wrong/avoidant MCQ answer | Bonus punishment message | 5 seconds |
| Instagram closed | Session summary | Dismissible |

### Escalation Schedule

| Session Time | Overlay Interval |
|---|---|
| 0–10 min | Every 10 min |
| 10–20 min | Every 7 min |
| 20–30 min | Every 5 min |
| 30–45 min | Every 3 min |
| 45–60 min | Every 2 min |
| 60 min+ | Every 1 min |

---

## 2. ARCHITECTURE OVERVIEW

```
com.conscience.app/
├── data/
│   ├── db/
│   │   ├── AppDatabase.kt
│   │   ├── SessionDao.kt
│   │   └── SessionEntity.kt
│   ├── prefs/
│   │   └── AppPreferences.kt
│   └── repository/
│       └── SessionRepository.kt
├── service/
│   ├── ConscienceAccessibilityService.kt
│   └── ConscienceForegroundService.kt
├── overlay/
│   ├── OverlayEngine.kt
│   ├── EntryInterventionActivity.kt
│   └── RealityCheckOverlayActivity.kt
├── questions/
│   ├── QuestionBank.kt
│   └── models/
│       ├── Question.kt
│       └── AnswerResult.kt
├── session/
│   └── SessionManager.kt
├── stats/
│   └── StatsEngine.kt
├── ui/
│   ├── main/
│   │   └── MainActivity.kt
│   ├── onboarding/
│   │   └── OnboardingActivity.kt
│   ├── settings/
│   │   └── SettingsActivity.kt
│   └── dashboard/
│       └── DashboardFragment.kt
├── security/
│   └── DataSecurityManager.kt
└── utils/
    ├── Constants.kt
    └── Extensions.kt
```

---

## 3. PROJECT SETUP

### 3.1 Android Studio Configuration
- Android Studio: Hedgehog (2023.1.1) or newer
- Minimum SDK: 26 (Android 8.0)
- Target SDK: 34 (Android 14)
- Compile SDK: 34
- Kotlin version: 1.9.0
- Java compatibility: JavaVersion.VERSION_17

### 3.2 Constants.kt — Create This First

```kotlin
// app/src/main/java/com/conscience/app/utils/Constants.kt
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

    // Notification
    const val NOTIFICATION_CHANNEL_ID = "conscience_service_channel"
    const val NOTIFICATION_ID = 1001
    const val FOREGROUND_SERVICE_NOTIFICATION_ID = 1002

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

    // Database
    const val DATABASE_NAME = "conscience_db"
    const val DATABASE_VERSION = 1
}
```

---

## 4. PERMISSIONS & MANIFEST

### AndroidManifest.xml — Complete File

```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    package="com.conscience.app">

    <!-- Core permissions -->
    <uses-permission android:name="android.permission.FOREGROUND_SERVICE" />
    <uses-permission android:name="android.permission.FOREGROUND_SERVICE_SPECIAL_USE" />
    <uses-permission android:name="android.permission.SYSTEM_ALERT_WINDOW" />
    <uses-permission android:name="android.permission.PACKAGE_USAGE_STATS"
        tools:ignore="ProtectedPermissions" />
    <uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED" />
    <uses-permission android:name="android.permission.POST_NOTIFICATIONS" />
    <uses-permission android:name="android.permission.VIBRATE" />

    <!-- Security: No network access needed -->
    <!-- EXPLICITLY no INTERNET permission — all data stays on device -->

    <application
        android:name=".ConscienceApplication"
        android:allowBackup="false"
        android:dataExtractionRules="@xml/data_extraction_rules"
        android:fullBackupContent="false"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:supportsRtl="true"
        android:theme="@style/Theme.Conscience"
        tools:targetApi="31">

        <!-- Main entry activity -->
        <activity
            android:name=".ui.main.MainActivity"
            android:exported="true"
            android:launchMode="singleTop">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>

        <!-- Onboarding -->
        <activity
            android:name=".ui.onboarding.OnboardingActivity"
            android:exported="false"
            android:theme="@style/Theme.Conscience.Fullscreen" />

        <!-- Entry Intervention — CRITICAL: must be able to show over other apps -->
        <activity
            android:name=".overlay.EntryInterventionActivity"
            android:exported="false"
            android:excludeFromRecents="true"
            android:launchMode="singleInstance"
            android:showOnLockScreen="true"
            android:theme="@style/Theme.Conscience.Overlay"
            android:taskAffinity="com.conscience.app.overlay" />

        <!-- Reality Check Overlay -->
        <activity
            android:name=".overlay.RealityCheckOverlayActivity"
            android:exported="false"
            android:excludeFromRecents="true"
            android:launchMode="singleInstance"
            android:showOnLockScreen="true"
            android:theme="@style/Theme.Conscience.Overlay"
            android:taskAffinity="com.conscience.app.overlay" />

        <!-- Settings -->
        <activity
            android:name=".ui.settings.SettingsActivity"
            android:exported="false" />

        <!-- Accessibility Service — THE CORE ENGINE -->
        <service
            android:name=".service.ConscienceAccessibilityService"
            android:exported="true"
            android:label="@string/accessibility_service_label"
            android:permission="android.permission.BIND_ACCESSIBILITY_SERVICE">
            <intent-filter>
                <action android:name="android.accessibilityservice.AccessibilityService" />
            </intent-filter>
            <meta-data
                android:name="android.accessibilityservice"
                android:resource="@xml/accessibility_service_config" />
        </service>

        <!-- Foreground Service -->
        <service
            android:name=".service.ConscienceForegroundService"
            android:exported="false"
            android:foregroundServiceType="specialUse" />

        <!-- Boot receiver -->
        <receiver
            android:name=".service.BootReceiver"
            android:exported="true">
            <intent-filter>
                <action android:name="android.intent.action.BOOT_COMPLETED" />
            </intent-filter>
        </receiver>

    </application>
</manifest>
```

### res/xml/accessibility_service_config.xml

```xml
<?xml version="1.0" encoding="utf-8"?>
<accessibility-service
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:accessibilityEventTypes="typeWindowStateChanged|typeWindowContentChanged"
    android:accessibilityFeedbackType="feedbackGeneric"
    android:accessibilityFlags="flagDefault"
    android:canRetrieveWindowContent="false"
    android:description="@string/accessibility_service_description"
    android:notificationTimeout="100"
    android:packageNames="com.instagram.android"
    android:settingsActivity=".ui.settings.SettingsActivity" />
```

### res/xml/data_extraction_rules.xml

```xml
<?xml version="1.0" encoding="utf-8"?>
<data-extraction-rules>
    <!-- Block ALL backup — data stays on device only -->
    <cloud-backup>
        <exclude domain="root" />
        <exclude domain="file" />
        <exclude domain="database" />
        <exclude domain="sharedpref" />
        <exclude domain="external" />
    </cloud-backup>
    <device-transfer>
        <exclude domain="root" />
        <exclude domain="file" />
        <exclude domain="database" />
        <exclude domain="sharedpref" />
    </device-transfer>
</data-extraction-rules>
```

---

## 5. DATA LAYER

### 5.1 SessionEntity.kt

```kotlin
// app/src/main/java/com/conscience/app/data/db/SessionEntity.kt
package com.conscience.app.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sessions")
data class SessionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val startTimeMs: Long,
    val endTimeMs: Long = 0,
    val durationMs: Long = 0,
    val openCount: Int = 1,
    val questionsAnswered: Int = 0,
    val avoidantAnswersCount: Int = 0,  // "I'll start after this" type answers
    val dateKey: String = "",            // "2024-01-15" format for daily grouping
    val closedManually: Boolean = false  // Did user close Instagram or just navigate away?
)
```

### 5.2 SessionDao.kt

```kotlin
// app/src/main/java/com/conscience/app/data/db/SessionDao.kt
package com.conscience.app.data.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface SessionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(session: SessionEntity): Long

    @Update
    suspend fun updateSession(session: SessionEntity)

    @Query("SELECT * FROM sessions WHERE dateKey = :dateKey ORDER BY startTimeMs DESC")
    fun getSessionsByDate(dateKey: String): Flow<List<SessionEntity>>

    @Query("SELECT SUM(durationMs) FROM sessions WHERE dateKey = :dateKey")
    suspend fun getTotalDurationForDate(dateKey: String): Long?

    @Query("SELECT COUNT(*) FROM sessions WHERE dateKey = :dateKey")
    suspend fun getOpenCountForDate(dateKey: String): Int

    @Query("SELECT * FROM sessions ORDER BY startTimeMs DESC LIMIT 1")
    suspend fun getLastSession(): SessionEntity?

    @Query("SELECT SUM(questionsAnswered) FROM sessions WHERE dateKey = :dateKey")
    suspend fun getQuestionsAnsweredForDate(dateKey: String): Int?

    @Query("DELETE FROM sessions WHERE startTimeMs < :cutoffMs")
    suspend fun deleteOldSessions(cutoffMs: Long)
}
```

### 5.3 AppDatabase.kt

```kotlin
// app/src/main/java/com/conscience/app/data/db/AppDatabase.kt
package com.conscience.app.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory
import com.conscience.app.utils.Constants

@Database(
    entities = [SessionEntity::class],
    version = Constants.DATABASE_VERSION,
    exportSchema = false   // SECURITY: Never export schema
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun sessionDao(): SessionDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                // SECURITY: Encrypt database with SQLCipher
                val passphrase = SQLiteDatabase.getBytes(
                    android.util.Base64.encodeToString(
                        context.packageName.toByteArray(),
                        android.util.Base64.NO_WRAP
                    ).toCharArray()
                )
                val factory = SupportFactory(passphrase)

                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    Constants.DATABASE_NAME
                )
                    .openHelperFactory(factory)
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
```

### 5.4 AppPreferences.kt

```kotlin
// app/src/main/java/com/conscience/app/data/prefs/AppPreferences.kt
package com.conscience.app.data.prefs

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.conscience.app.utils.Constants
import java.time.LocalDate

class AppPreferences(context: Context) {

    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    // SECURITY: Use EncryptedSharedPreferences
    private val prefs: SharedPreferences = EncryptedSharedPreferences.create(
        context,
        Constants.PREFS_NAME,
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    var isAppEnabled: Boolean
        get() = prefs.getBoolean(Constants.KEY_APP_ENABLED, true)
        set(value) = prefs.edit().putBoolean(Constants.KEY_APP_ENABLED, value).apply()

    var isOnboardingComplete: Boolean
        get() = prefs.getBoolean(Constants.KEY_ONBOARDING_COMPLETE, false)
        set(value) = prefs.edit().putBoolean(Constants.KEY_ONBOARDING_COMPLETE, value).apply()

    var dailyOpenCount: Int
        get() {
            resetIfNewDay()
            return prefs.getInt(Constants.KEY_DAILY_OPEN_COUNT, 0)
        }
        set(value) = prefs.edit().putInt(Constants.KEY_DAILY_OPEN_COUNT, value).apply()

    var dailyTimeMs: Long
        get() {
            resetIfNewDay()
            return prefs.getLong(Constants.KEY_DAILY_TIME_MS, 0L)
        }
        set(value) = prefs.edit().putLong(Constants.KEY_DAILY_TIME_MS, value).apply()

    var totalOpensAllTime: Int
        get() = prefs.getInt(Constants.KEY_TOTAL_OPENS_ALLTIME, 0)
        set(value) = prefs.edit().putInt(Constants.KEY_TOTAL_OPENS_ALLTIME, value).apply()

    private fun resetIfNewDay() {
        val today = LocalDate.now().toString()
        val lastReset = prefs.getString(Constants.KEY_LAST_RESET_DATE, "")
        if (lastReset != today) {
            prefs.edit()
                .putString(Constants.KEY_LAST_RESET_DATE, today)
                .putInt(Constants.KEY_DAILY_OPEN_COUNT, 0)
                .putLong(Constants.KEY_DAILY_TIME_MS, 0L)
                .apply()
        }
    }

    fun incrementDailyOpenCount() {
        dailyOpenCount = dailyOpenCount + 1
        totalOpensAllTime = totalOpensAllTime + 1
    }
}
```

---

## 6. ACCESSIBILITY SERVICE

> **AGENT NOTE**: This is the most critical component. It must be implemented exactly as written. The `onAccessibilityEvent` function is the trigger for everything.

```kotlin
// app/src/main/java/com/conscience/app/service/ConscienceAccessibilityService.kt
package com.conscience.app.service

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.content.Intent
import android.view.accessibility.AccessibilityEvent
import com.conscience.app.session.SessionManager
import com.conscience.app.utils.Constants

class ConscienceAccessibilityService : AccessibilityService() {

    private var currentPackage: String = ""
    private var instagramStartTime: Long = 0L
    private var isInstagramActive: Boolean = false

    override fun onServiceConnected() {
        super.onServiceConnected()
        val info = AccessibilityServiceInfo().apply {
            eventTypes = AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED or
                         AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED
            feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC
            flags = AccessibilityServiceInfo.FLAG_INCLUDE_NOT_IMPORTANT_VIEWS
            notificationTimeout = 100
            packageNames = arrayOf(Constants.INSTAGRAM_PACKAGE)
        }
        serviceInfo = info
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        event ?: return

        if (event.eventType != AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) return

        val packageName = event.packageName?.toString() ?: return

        when {
            packageName == Constants.INSTAGRAM_PACKAGE && !isInstagramActive -> {
                // Instagram just came to foreground
                onInstagramOpened()
            }
            packageName != Constants.INSTAGRAM_PACKAGE && isInstagramActive -> {
                // User left Instagram
                onInstagramClosed()
            }
        }

        currentPackage = packageName
    }

    private fun onInstagramOpened() {
        isInstagramActive = true
        instagramStartTime = System.currentTimeMillis()

        // Notify SessionManager
        SessionManager.getInstance(applicationContext).onInstagramOpened()

        // Fire Entry Intervention
        val intent = Intent(this, EntryInterventionActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or
                     Intent.FLAG_ACTIVITY_SINGLE_TOP or
                     Intent.FLAG_ACTIVITY_NO_HISTORY)
        }
        startActivity(intent)
    }

    private fun onInstagramClosed() {
        isInstagramActive = false
        val sessionDurationMs = System.currentTimeMillis() - instagramStartTime

        // Notify SessionManager
        SessionManager.getInstance(applicationContext).onInstagramClosed(sessionDurationMs)

        // Broadcast for overlay cleanup
        sendBroadcast(Intent(Constants.ACTION_INSTAGRAM_CLOSED))
    }

    override fun onInterrupt() {
        isInstagramActive = false
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isInstagramActive) {
            onInstagramClosed()
        }
    }
}
```

### BootReceiver.kt

```kotlin
// app/src/main/java/com/conscience/app/service/BootReceiver.kt
package com.conscience.app.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            // Accessibility service auto-starts if enabled in settings
            // We just need the foreground service for notification
            val serviceIntent = Intent(context, ConscienceForegroundService::class.java)
            context.startForegroundService(serviceIntent)
        }
    }
}
```

---

## 7. SESSION MANAGER

```kotlin
// app/src/main/java/com/conscience/app/session/SessionManager.kt
package com.conscience.app.session

import android.content.Context
import com.conscience.app.data.db.AppDatabase
import com.conscience.app.data.db.SessionEntity
import com.conscience.app.data.prefs.AppPreferences
import com.conscience.app.utils.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDate

class SessionManager private constructor(private val context: Context) {

    private val prefs = AppPreferences(context)
    private val db = AppDatabase.getInstance(context)
    private val scope = CoroutineScope(Dispatchers.IO + Job())

    private var sessionStartTime = 0L
    private var currentSessionId = 0L
    private var overlayTimerJob: Job? = null
    private var overlayCallback: (() -> Unit)? = null

    // Listeners
    var onOverlayTrigger: (() -> Unit)? = null

    companion object {
        @Volatile
        private var INSTANCE: SessionManager? = null

        fun getInstance(context: Context): SessionManager {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: SessionManager(context.applicationContext).also { INSTANCE = it }
            }
        }
    }

    fun onInstagramOpened() {
        sessionStartTime = System.currentTimeMillis()
        prefs.incrementDailyOpenCount()

        scope.launch {
            val entity = SessionEntity(
                startTimeMs = sessionStartTime,
                dateKey = LocalDate.now().toString()
            )
            currentSessionId = db.sessionDao().insertSession(entity)
        }

        startEscalatingTimer()
    }

    fun onInstagramClosed(durationMs: Long) {
        overlayTimerJob?.cancel()

        scope.launch {
            val session = db.sessionDao().getLastSession()
            session?.let {
                db.sessionDao().updateSession(
                    it.copy(
                        endTimeMs = System.currentTimeMillis(),
                        durationMs = durationMs,
                        closedManually = true
                    )
                )
            }
        }

        // Update daily total
        prefs.dailyTimeMs = prefs.dailyTimeMs + durationMs
    }

    fun recordQuestionAnswered(wasAvoidant: Boolean) {
        scope.launch {
            val session = db.sessionDao().getLastSession()
            session?.let {
                db.sessionDao().updateSession(
                    it.copy(
                        questionsAnswered = it.questionsAnswered + 1,
                        avoidantAnswersCount = if (wasAvoidant)
                            it.avoidantAnswersCount + 1 else it.avoidantAnswersCount
                    )
                )
            }
        }
    }

    private fun startEscalatingTimer() {
        overlayTimerJob?.cancel()
        overlayTimerJob = scope.launch {
            while (true) {
                val sessionDuration = System.currentTimeMillis() - sessionStartTime
                val intervalMs = getIntervalForDuration(sessionDuration)
                delay(intervalMs)
                onOverlayTrigger?.invoke()
            }
        }
    }

    fun getIntervalForDuration(durationMs: Long): Long {
        return when {
            durationMs < 10 * 60 * 1000L  -> 10 * 60 * 1000L
            durationMs < 20 * 60 * 1000L  -> 7 * 60 * 1000L
            durationMs < 30 * 60 * 1000L  -> 5 * 60 * 1000L
            durationMs < 45 * 60 * 1000L  -> 3 * 60 * 1000L
            durationMs < 60 * 60 * 1000L  -> 2 * 60 * 1000L
            else                           -> 1 * 60 * 1000L
        }
    }

    fun getSessionDurationMs(): Long =
        if (sessionStartTime > 0) System.currentTimeMillis() - sessionStartTime else 0L

    fun getDailyTimeMs(): Long = prefs.dailyTimeMs
    fun getDailyOpenCount(): Int = prefs.dailyOpenCount
    fun getTotalOpens(): Int = prefs.totalOpensAllTime

    fun formatDuration(ms: Long): String {
        val totalSeconds = ms / 1000
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60
        return if (minutes > 0) "${minutes}m ${seconds}s" else "${seconds}s"
    }
}
```

---

## 8. OVERLAY ENGINE

```kotlin
// app/src/main/java/com/conscience/app/overlay/OverlayEngine.kt
package com.conscience.app.overlay

import android.content.Context
import android.content.Intent
import com.conscience.app.overlay.EntryInterventionActivity
import com.conscience.app.overlay.RealityCheckOverlayActivity
import com.conscience.app.service.ConscienceAccessibilityService

object OverlayEngine {

    fun showEntryIntervention(context: Context) {
        val intent = Intent(context, EntryInterventionActivity::class.java).apply {
            addFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK or
                Intent.FLAG_ACTIVITY_CLEAR_TOP or
                Intent.FLAG_ACTIVITY_SINGLE_TOP
            )
        }
        context.startActivity(intent)
    }

    fun showRealityCheck(context: Context) {
        val intent = Intent(context, RealityCheckOverlayActivity::class.java).apply {
            addFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK or
                Intent.FLAG_ACTIVITY_SINGLE_TOP
            )
        }
        context.startActivity(intent)
    }
}
```

---

## 9. ENTRY INTERVENTION SCREEN

```kotlin
// app/src/main/java/com/conscience/app/overlay/EntryInterventionActivity.kt
package com.conscience.app.overlay

import android.app.Activity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.conscience.app.questions.QuestionBank
import com.conscience.app.session.SessionManager
import com.conscience.app.utils.Constants

class EntryInterventionActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Make it truly fullscreen and above everything
        window.addFlags(
            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON or
            WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD or
            WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
            WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
        )

        // Disable back button behavior
        onBackPressedDispatcher

        val message = QuestionBank.getRandomEntryMessage()
        val sessionManager = SessionManager.getInstance(applicationContext)

        setContent {
            EntryInterventionScreen(
                message = message,
                sessionDurationFormatted = sessionManager.formatDuration(
                    sessionManager.getDailyTimeMs()
                ),
                dailyOpens = sessionManager.getDailyOpenCount(),
                onTimerFinished = { finish() }
            )
        }
    }

    // CRITICAL: Block back button during intervention
    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        // Do nothing — intervention cannot be dismissed
    }
}

@Composable
fun EntryInterventionScreen(
    message: String,
    sessionDurationFormatted: String,
    dailyOpens: Int,
    onTimerFinished: () -> Unit
) {
    var timeRemaining by remember { mutableStateOf(20) }
    var canDismiss by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        for (i in 20 downTo 1) {
            timeRemaining = i
            kotlinx.coroutines.delay(1000)
        }
        canDismiss = true
        onTimerFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0A0A0A)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Stats bar at top
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Spacer(modifier = Modifier.height(48.dp))
                Text(
                    text = "TODAY: $dailyOpens opens • $sessionDurationFormatted wasted",
                    color = Color(0xFF666666),
                    fontSize = 12.sp,
                    letterSpacing = 1.5.sp
                )
            }

            // Main message
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "⚠",
                    fontSize = 48.sp,
                    color = Color(0xFFFF3B30)
                )
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = message,
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    lineHeight = 32.sp
                )
            }

            // Countdown
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = if (!canDismiss) "$timeRemaining" else "✓",
                    color = Color(0xFFFF3B30),
                    fontSize = 64.sp,
                    fontWeight = FontWeight.Black
                )
                Text(
                    text = if (!canDismiss) "seconds until Instagram opens" else "Continuing to Instagram...",
                    color = Color(0xFF888888),
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(48.dp))
            }
        }
    }
}
```

---

## 10. CONTINUOUS REALITY CHECK OVERLAY

```kotlin
// app/src/main/java/com/conscience/app/overlay/RealityCheckOverlayActivity.kt
package com.conscience.app.overlay

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.conscience.app.questions.QuestionBank
import com.conscience.app.questions.models.AnswerResult
import com.conscience.app.session.SessionManager

class RealityCheckOverlayActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.addFlags(
            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON or
            WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
        )

        val sessionManager = SessionManager.getInstance(applicationContext)
        val question = QuestionBank.getNextQuestion()

        setContent {
            RealityCheckScreen(
                question = question,
                sessionDurationMs = sessionManager.getSessionDurationMs(),
                onAnswerSelected = { answer ->
                    val result = QuestionBank.evaluateAnswer(question.id, answer)
                    sessionManager.recordQuestionAnswered(result.isAvoidant)
                    if (result.hasPunishment) {
                        showBonusPunishment(result.punishmentMessage)
                    } else {
                        finish()
                    }
                }
            )
        }
    }

    private fun showBonusPunishment(message: String) {
        setContent {
            BonusPunishmentScreen(
                message = message,
                onFinished = { finish() }
            )
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        // Block back — must answer
    }
}

@Composable
fun RealityCheckScreen(
    question: com.conscience.app.questions.models.Question,
    sessionDurationMs: Long,
    onAnswerSelected: (String) -> Unit
) {
    val minutes = sessionDurationMs / 60000
    var selectedAnswer by remember { mutableStateOf<String?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xF0111111))  // Semi-transparent black
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            // Timer badge
            Surface(
                color = Color(0xFFFF3B30).copy(alpha = 0.15f),
                shape = RoundedCornerShape(20.dp)
            ) {
                Text(
                    text = "⏱  $minutes min on Instagram",
                    color = Color(0xFFFF3B30),
                    fontSize = 13.sp,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Category label
            Text(
                text = question.category.uppercase(),
                color = Color(0xFF555555),
                fontSize = 11.sp,
                letterSpacing = 2.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Question
            Text(
                text = question.text,
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                lineHeight = 30.sp
            )

            Spacer(modifier = Modifier.height(40.dp))

            // MCQ Options
            question.options.forEachIndexed { index, option ->
                val isSelected = selectedAnswer == option
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                        .clickable {
                            selectedAnswer = option
                        },
                    color = if (isSelected) Color(0xFFFF3B30).copy(alpha = 0.2f)
                            else Color(0xFF1E1E1E),
                    shape = RoundedCornerShape(12.dp),
                    border = if (isSelected) BorderStroke(1.dp, Color(0xFFFF3B30))
                             else BorderStroke(1.dp, Color(0xFF333333))
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${('A' + index)}.",
                            color = if (isSelected) Color(0xFFFF3B30) else Color(0xFF666666),
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.width(28.dp)
                        )
                        Text(
                            text = option,
                            color = if (isSelected) Color.White else Color(0xFFCCCCCC),
                            fontSize = 15.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Submit button — only active when option selected
            Button(
                onClick = { selectedAnswer?.let { onAnswerSelected(it) } },
                enabled = selectedAnswer != null,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFF3B30),
                    disabledContainerColor = Color(0xFF333333)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "Answer to continue",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
fun BonusPunishmentScreen(message: String, onFinished: () -> Unit) {
    var timeLeft by remember { mutableStateOf(5) }

    LaunchedEffect(Unit) {
        for (i in 5 downTo 1) {
            timeLeft = i
            kotlinx.coroutines.delay(1000)
        }
        onFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1A0000)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(32.dp)
        ) {
            Text("🔴", fontSize = 48.sp)
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = message,
                color = Color(0xFFFF6B6B),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                lineHeight = 30.sp
            )
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = "$timeLeft",
                color = Color(0xFF660000),
                fontSize = 48.sp,
                fontWeight = FontWeight.Black
            )
        }
    }
}
```

---

## 11. MCQ QUESTION BANK

### Question.kt

```kotlin
// app/src/main/java/com/conscience/app/questions/models/Question.kt
package com.conscience.app.questions.models

data class Question(
    val id: String,
    val category: String,
    val text: String,
    val options: List<String>,
    val avoidantOptions: List<String> = emptyList(),  // These trigger punishment
    val punishmentMap: Map<String, String> = emptyMap()  // option -> punishment message
)
```

### AnswerResult.kt

```kotlin
// app/src/main/java/com/conscience/app/questions/models/AnswerResult.kt
package com.conscience.app.questions.models

data class AnswerResult(
    val isAvoidant: Boolean,
    val hasPunishment: Boolean,
    val punishmentMessage: String = ""
)
```

### QuestionBank.kt — Complete Question Set

```kotlin
// app/src/main/java/com/conscience/app/questions/QuestionBank.kt
package com.conscience.app.questions

import com.conscience.app.questions.models.AnswerResult
import com.conscience.app.questions.models.Question

object QuestionBank {

    // Entry intervention messages — shown as fullscreen shock on opening Instagram
    private val ENTRY_MESSAGES = listOf(
        "You just opened Instagram.\nSomeone your age is building something right now.",
        "This app has trained you to reach for it without thinking.\nYou just proved it.",
        "Instagram made \$0.003 from your last session.\nWhat did you make?",
        "The average person loses 2.5 hours a day to this.\nAre you average?",
        "Every scroll you do trains the algorithm.\nWhat is the algorithm training you to do?",
        "You came back.\nWhat specifically are you hoping to find here?",
        "Right now, you could read 3 pages of a book.\nYou chose this instead.",
        "Your attention is the product being sold.\nYou're voluntarily showing up to be sold.",
        "The people who built this app don't let their children use it.\nThink about that.",
        "Three years ago your attention span was longer.\nThis app shortened it.",
        "You're about to spend time you'll never get back.\nOn what, exactly?",
        "Boredom is where creativity lives.\nYou're about to kill it again.",
        "The moment you feel discomfort, you reach for this.\nThat reflex is the problem.",
        "Dopamine hits from Instagram last 3 minutes.\nThe regret lasts longer.",
        "Your future self is watching this decision.\nWhat are they thinking?"
    )

    // Full question bank
    private val QUESTIONS = listOf(

        // === ABILITY EROSION ===
        Question(
            id = "ability_1",
            category = "Ability Erosion",
            text = "When did you last read something longer than a caption — and actually finished it?",
            options = listOf(
                "Today or yesterday",
                "This week",
                "This month",
                "I can't remember"
            ),
            avoidantOptions = listOf("I can't remember"),
            punishmentMap = mapOf(
                "I can't remember" to "Instagram didn't steal your focus overnight.\nIt took it one scroll at a time. Every day."
            )
        ),

        Question(
            id = "ability_2",
            category = "Ability Erosion",
            text = "Your attention span — honestly comparing 3 years ago to now:",
            options = listOf(
                "Same or better",
                "Slightly shorter",
                "Noticeably shorter",
                "I struggle to focus on anything for long"
            ),
            avoidantOptions = listOf("I struggle to focus on anything for long"),
            punishmentMap = mapOf(
                "I struggle to focus on anything for long" to "That difficulty is not random.\nIt was engineered. By this app.\nAnd you keep coming back.",
                "Noticeably shorter" to "You noticed it getting worse.\nAnd you're still here."
            )
        ),

        Question(
            id = "ability_3",
            category = "Ability Erosion",
            text = "If Instagram disappeared tomorrow, which ability would grow back?",
            options = listOf(
                "Deep focus and concentration",
                "Patience and boredom tolerance",
                "Original creative thinking",
                "All of the above"
            )
        ),

        Question(
            id = "ability_4",
            category = "Ability Erosion",
            text = "How many Reels do you remember from last week?",
            options = listOf(
                "Several — I remember them clearly",
                "A few vague ones",
                "One or two",
                "None at all"
            ),
            avoidantOptions = listOf("None at all"),
            punishmentMap = mapOf(
                "None at all" to "You gave hours to this app last week.\nIt gave you nothing back worth keeping."
            )
        ),

        // === TIME ACCOUNTABILITY ===
        Question(
            id = "time_1",
            category = "Time Accountability",
            text = "Name one thing you planned to do today that isn't done yet.",
            options = listOf(
                "Nothing — I'm on top of everything",
                "One small thing I can do right after this",
                "Something important I keep postponing",
                "Multiple things I've been avoiding"
            ),
            avoidantOptions = listOf("Multiple things I've been avoiding", "Something important I keep postponing"),
            punishmentMap = mapOf(
                "Multiple things I've been avoiding" to "Instagram is where you go to avoid your life.\nYour list is still waiting.",
                "Something important I keep postponing" to "Postponing it again won't make it easier.\nIt'll make it heavier."
            )
        ),

        Question(
            id = "time_2",
            category = "Time Accountability",
            text = "What would your 10-years-older self say about this specific moment?",
            options = listOf(
                "\"Good job staying informed\"",
                "\"That was harmless fun\"",
                "\"You had more important things to do\"",
                "\"I wish you'd used that time differently\""
            ),
            avoidantOptions = listOf("I wish you'd used that time differently"),
            punishmentMap = mapOf(
                "I wish you'd used that time differently" to "You already know the answer.\nYou just chose to come here anyway."
            )
        ),

        Question(
            id = "time_3",
            category = "Time Accountability",
            text = "At your current usage rate, you'll spend approximately 15 days on Instagram this year. Your reaction?",
            options = listOf(
                "That's fine, it's how I relax",
                "That seems high but I don't think about it",
                "That's disturbing when I think about it",
                "I already knew and I hate it"
            ),
            avoidantOptions = listOf("That seems high but I don't think about it"),
            punishmentMap = mapOf(
                "That seems high but I don't think about it" to "Not thinking about it is the strategy.\nInstagram counts on exactly that."
            )
        ),

        // === REAL LIFE DISPLACEMENT ===
        Question(
            id = "real_1",
            category = "Real Life",
            text = "Is there a person physically near you right now that you haven't spoken to?",
            options = listOf(
                "No, I'm alone",
                "Yes, but they're busy too",
                "Yes — and I chose this instead",
                "Yes — and that's become normal"
            ),
            avoidantOptions = listOf("Yes — and that's become normal"),
            punishmentMap = mapOf(
                "Yes — and that's become normal" to "When being with people while ignoring them becomes normal,\ninstagram has already won."
            )
        ),

        Question(
            id = "real_2",
            category = "Real Life",
            text = "When did you last do something meaningful that you didn't document or share?",
            options = listOf(
                "Today",
                "This week",
                "I struggle to do things without thinking about sharing",
                "I can't remember"
            ),
            avoidantOptions = listOf("I struggle to do things without thinking about sharing"),
            punishmentMap = mapOf(
                "I struggle to do things without thinking about sharing" to "When you live for the post,\nyou stop living for the moment.\nInstagram colonized your present tense."
            )
        ),

        Question(
            id = "real_3",
            category = "Real Life",
            text = "What's actually happening in the physical world around you right now?",
            options = listOf(
                "Something interesting I'm ignoring",
                "People I could be connecting with",
                "Work or tasks I'm avoiding",
                "Nothing — this moment genuinely has nothing better"
            )
        ),

        // === GOAL CONFRONTATION ===
        Question(
            id = "goal_1",
            category = "Goal Confrontation",
            text = "Pick the most honest reason you opened Instagram right now:",
            options = listOf(
                "I'm genuinely bored and relaxing",
                "I'm lonely and this fills it temporarily",
                "I'm avoiding something specific",
                "Pure habit — I didn't even decide to"
            ),
            avoidantOptions = listOf("I'm avoiding something specific", "Pure habit — I didn't even decide to"),
            punishmentMap = mapOf(
                "I'm avoiding something specific" to "Instagram won't dissolve what you're avoiding.\nIt'll still be there when you close this.\nBigger now.",
                "Pure habit — I didn't even decide to" to "You didn't decide.\nThe habit decided for you.\nThat's not freedom. That's conditioning."
            )
        ),

        Question(
            id = "goal_2",
            category = "Goal Confrontation",
            text = "The thing you keep saying you'll start 'soon' — how long have you been saying that?",
            options = listOf(
                "A few days — I'll actually start this week",
                "A few weeks",
                "A few months",
                "Over a year"
            ),
            avoidantOptions = listOf("Over a year", "A few months"),
            punishmentMap = mapOf(
                "Over a year" to "A year of 'soon'.\nInstagram had a great year.\nDid you?",
                "A few months" to "Months pass faster than you think.\nAnd Instagram is very good at filling them."
            )
        ),

        Question(
            id = "goal_3",
            category = "Goal Confrontation",
            text = "Instagram remembers every post you've ever liked. What have YOU created lately?",
            options = listOf(
                "Something I'm proud of",
                "Small things, but something",
                "Nothing recently, but I have ideas",
                "Nothing. This is where my creative energy goes."
            ),
            avoidantOptions = listOf("Nothing. This is where my creative energy goes."),
            punishmentMap = mapOf(
                "Nothing. This is where my creative energy goes." to "Consuming creativity and creating are opposites.\nEvery hour here is an hour not building anything.\nYou know this."
            )
        ),

        Question(
            id = "goal_4",
            category = "Goal Confrontation",
            text = "Mental health check: How has Instagram made you feel in the last week?",
            options = listOf(
                "Generally fine or positive",
                "Occasionally inadequate comparing myself to others",
                "Anxious about my own life after seeing others'",
                "Worse about myself but I keep coming back anyway"
            ),
            avoidantOptions = listOf("Worse about myself but I keep coming back anyway"),
            punishmentMap = mapOf(
                "Worse about myself but I keep coming back anyway" to "This is the trap.\nIt makes you feel bad.\nAnd it's engineered to make you come back despite that.\nYou're in the trap right now."
            )
        )
    )

    private var lastUsedQuestionIndex = -1
    private val usedQuestionsInSession = mutableSetOf<String>()

    fun getRandomEntryMessage(): String = ENTRY_MESSAGES.random()

    fun getNextQuestion(): Question {
        // Avoid repeating questions in same session
        val available = QUESTIONS.filter { it.id !in usedQuestionsInSession }
        val pool = if (available.isEmpty()) {
            usedQuestionsInSession.clear()
            QUESTIONS
        } else {
            available
        }

        val question = pool.random()
        usedQuestionsInSession.add(question.id)
        return question
    }

    fun evaluateAnswer(questionId: String, answer: String): AnswerResult {
        val question = QUESTIONS.find { it.id == questionId }
            ?: return AnswerResult(false, false)

        val isAvoidant = answer in question.avoidantOptions
        val punishmentMessage = question.punishmentMap[answer]

        return AnswerResult(
            isAvoidant = isAvoidant,
            hasPunishment = punishmentMessage != null,
            punishmentMessage = punishmentMessage ?: ""
        )
    }
}
```

---

## 12. STATS ENGINE

```kotlin
// app/src/main/java/com/conscience/app/stats/StatsEngine.kt
package com.conscience.app.stats

import android.content.Context
import com.conscience.app.data.db.AppDatabase
import com.conscience.app.data.prefs.AppPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate

data class DailyStats(
    val date: String,
    val openCount: Int,
    val totalTimeMs: Long,
    val questionsAnswered: Int,
    val avoidantAnswers: Int
)

class StatsEngine(context: Context) {

    private val db = AppDatabase.getInstance(context)
    private val prefs = AppPreferences(context)

    fun getTodayStats(): Flow<DailyStats> {
        val today = LocalDate.now().toString()
        return db.sessionDao().getSessionsByDate(today).map { sessions ->
            DailyStats(
                date = today,
                openCount = sessions.size,
                totalTimeMs = sessions.sumOf { it.durationMs },
                questionsAnswered = sessions.sumOf { it.questionsAnswered },
                avoidantAnswers = sessions.sumOf { it.avoidantAnswersCount }
            )
        }
    }

    fun formatTimeMs(ms: Long): String {
        val totalMinutes = ms / 60000
        val hours = totalMinutes / 60
        val minutes = totalMinutes % 60
        return when {
            hours > 0 -> "${hours}h ${minutes}m"
            else -> "${minutes}m"
        }
    }

    // Clean up sessions older than 30 days for privacy
    suspend fun pruneOldData() {
        val cutoff = System.currentTimeMillis() - (30L * 24 * 60 * 60 * 1000)
        db.sessionDao().deleteOldSessions(cutoff)
    }
}
```

---

## 13. SETTINGS SCREEN

```kotlin
// app/src/main/java/com/conscience/app/ui/settings/SettingsActivity.kt
package com.conscience.app.ui.settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.conscience.app.data.prefs.AppPreferences

class SettingsActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val prefs = AppPreferences(applicationContext)

        setContent {
            var isEnabled by remember { mutableStateOf(prefs.isAppEnabled) }

            Scaffold { padding ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(24.dp)
                ) {
                    Text("Settings", style = MaterialTheme.typography.headlineMedium)
                    Spacer(modifier = Modifier.height(24.dp))

                    // Enable/Disable toggle
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("Conscience Active")
                            Text(
                                "Intercept Instagram opens",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Switch(
                            checked = isEnabled,
                            onCheckedChange = {
                                isEnabled = it
                                prefs.isAppEnabled = it
                            }
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    Divider()
                    Spacer(modifier = Modifier.height(16.dp))

                    // Accessibility settings shortcut
                    Button(
                        onClick = {
                            startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Open Accessibility Settings")
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Overlay permission shortcut
                    Button(
                        onClick = {
                            startActivity(
                                Intent(
                                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                                    Uri.parse("package:$packageName")
                                )
                            )
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Grant Draw Over Apps Permission")
                    }
                }
            }
        }
    }
}
```

---

## 14. MAIN ACTIVITY & ONBOARDING

```kotlin
// app/src/main/java/com/conscience/app/ui/main/MainActivity.kt
package com.conscience.app.ui.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.conscience.app.data.prefs.AppPreferences
import com.conscience.app.service.ConscienceForegroundService
import com.conscience.app.stats.StatsEngine
import com.conscience.app.ui.onboarding.OnboardingActivity

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val prefs = AppPreferences(applicationContext)

        // Route to onboarding if first launch
        if (!prefs.isOnboardingComplete) {
            startActivity(Intent(this, OnboardingActivity::class.java))
            finish()
            return
        }

        // Start foreground service
        startForegroundService(Intent(this, ConscienceForegroundService::class.java))

        val stats = StatsEngine(applicationContext)

        setContent {
            val todayStats by stats.getTodayStats().collectAsState(initial = null)
            val isAccessibilityEnabled = isAccessibilityServiceEnabled()
            val isOverlayEnabled = Settings.canDrawOverlays(this)

            MainScreen(
                todayOpens = todayStats?.openCount ?: 0,
                todayTime = stats.formatTimeMs(todayStats?.totalTimeMs ?: 0),
                questionsAnswered = todayStats?.questionsAnswered ?: 0,
                isAccessibilityEnabled = isAccessibilityEnabled,
                isOverlayEnabled = isOverlayEnabled,
                onEnableAccessibility = {
                    startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
                },
                onEnableOverlay = {
                    startActivity(
                        Intent(
                            Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                            Uri.parse("package:$packageName")
                        )
                    )
                }
            )
        }
    }

    private fun isAccessibilityServiceEnabled(): Boolean {
        val service = "${packageName}/.service.ConscienceAccessibilityService"
        val enabled = Settings.Secure.getString(
            contentResolver,
            Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
        ) ?: return false
        return enabled.contains(service)
    }
}

@Composable
fun MainScreen(
    todayOpens: Int,
    todayTime: String,
    questionsAnswered: Int,
    isAccessibilityEnabled: Boolean,
    isOverlayEnabled: Boolean,
    onEnableAccessibility: () -> Unit,
    onEnableOverlay: () -> Unit
) {
    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Conscience", style = MaterialTheme.typography.headlineLarge)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Today's Instagram Report", style = MaterialTheme.typography.bodyMedium)

            Spacer(modifier = Modifier.height(32.dp))

            // Stats cards
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                StatCard("Opens", todayOpens.toString(), Modifier.weight(1f))
                StatCard("Time", todayTime, Modifier.weight(1f))
                StatCard("Questions", questionsAnswered.toString(), Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Permission warnings
            if (!isAccessibilityEnabled) {
                WarningCard(
                    message = "Accessibility Service not enabled — app cannot detect Instagram",
                    buttonText = "Enable Now",
                    onClick = onEnableAccessibility
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            if (!isOverlayEnabled) {
                WarningCard(
                    message = "Draw over apps permission missing — overlays won't show",
                    buttonText = "Grant Permission",
                    onClick = onEnableOverlay
                )
            }
        }
    }
}

@Composable
fun StatCard(label: String, value: String, modifier: Modifier = Modifier) {
    Card(modifier = modifier) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(value, style = MaterialTheme.typography.headlineMedium)
            Text(label, style = MaterialTheme.typography.labelSmall)
        }
    }
}

@Composable
fun WarningCard(message: String, buttonText: String, onClick: () -> Unit) {
    Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(message, style = MaterialTheme.typography.bodySmall)
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = onClick) { Text(buttonText) }
        }
    }
}
```

---

## 15. SECURITY IMPLEMENTATION

```kotlin
// app/src/main/java/com/conscience/app/security/DataSecurityManager.kt
package com.conscience.app.security

import android.content.Context
import android.util.Log
import com.conscience.app.data.db.AppDatabase
import com.conscience.app.data.prefs.AppPreferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate

object DataSecurityManager {

    private const val TAG = "DataSecurityManager"

    // Run on app startup
    fun initialize(context: Context) {
        enforceDataRetention(context)
        verifyNoNetworkPermission(context)
    }

    // Delete data older than 30 days
    private fun enforceDataRetention(context: Context) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val db = AppDatabase.getInstance(context)
                val cutoff = System.currentTimeMillis() - (30L * 24 * 60 * 60 * 1000)
                db.sessionDao().deleteOldSessions(cutoff)
            } catch (e: Exception) {
                Log.e(TAG, "Data retention enforcement failed", e)
            }
        }
    }

    // Verify app has no internet permission at runtime
    private fun verifyNoNetworkPermission(context: Context) {
        val pm = context.packageManager
        val result = pm.checkPermission(
            android.Manifest.permission.INTERNET,
            context.packageName
        )
        if (result == android.content.pm.PackageManager.PERMISSION_GRANTED) {
            // This should NEVER happen — log as critical security issue
            Log.e(TAG, "SECURITY ALERT: Internet permission detected. Data may be at risk.")
        }
    }

    // Wipe all local data (for user-requested data deletion)
    fun wipeAllData(context: Context) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Clear database
                AppDatabase.getInstance(context).clearAllTables()

                // Clear preferences
                val prefs = context.getSharedPreferences("conscience_prefs", Context.MODE_PRIVATE)
                prefs.edit().clear().apply()

                Log.i(TAG, "All user data wiped successfully")
            } catch (e: Exception) {
                Log.e(TAG, "Data wipe failed", e)
            }
        }
    }
}
```

---

## 16. BUILD & GRADLE CONFIGURATION

### build.gradle (app level)

```kotlin
plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.conscience.app"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.conscience.app"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions { jvmTarget = "17" }

    buildFeatures { compose = true }
    composeOptions { kotlinCompilerExtensionVersion = "1.5.3" }
}

dependencies {
    // Compose
    implementation(platform("androidx.compose:compose-bom:2024.02.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.activity:activity-compose:1.8.2")

    // Room (local database)
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    ksp("androidx.room:room-compiler:2.6.1")

    // SQLCipher (database encryption)
    implementation("net.zetetic:android-database-sqlcipher:4.5.4")
    implementation("androidx.sqlite:sqlite-ktx:2.4.0")

    // Encrypted SharedPreferences
    implementation("androidx.security:security-crypto:1.1.0-alpha06")

    // Hilt (dependency injection)
    implementation("com.google.dagger:hilt-android:2.50")
    ksp("com.google.dagger:hilt-compiler:2.50")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    // DataStore
    implementation("androidx.datastore:datastore-preferences:1.0.0")

    // Lifecycle
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
}
```

---

## 17. PHASE ROLLOUT PLAN

### Phase 1 — MVP (Implement First)
Files to implement in order:
1. `Constants.kt`
2. `AndroidManifest.xml` + `accessibility_service_config.xml`
3. `SessionEntity.kt` + `SessionDao.kt` + `AppDatabase.kt`
4. `AppPreferences.kt`
5. `ConscienceAccessibilityService.kt`
6. `SessionManager.kt`
7. `QuestionBank.kt` (entry messages only)
8. `EntryInterventionActivity.kt`
9. `MainActivity.kt` (basic version)

### Phase 2 — Reality Checks
10. `RealityCheckOverlayActivity.kt`
11. `QuestionBank.kt` (full MCQ set)
12. `OverlayEngine.kt`
13. Session escalation timer in `SessionManager.kt`

### Phase 3 — Stats & Security
14. `StatsEngine.kt`
15. `DataSecurityManager.kt`
16. `SettingsActivity.kt`
17. Dashboard in `MainActivity.kt`

---

*END OF PRD*
