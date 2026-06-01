package com.conscience.app.session

import android.content.Context
import com.conscience.app.data.db.AppDatabase
import com.conscience.app.data.db.SessionEntity
import com.conscience.app.data.prefs.AppPreferences
import com.conscience.app.utils.Constants
import com.conscience.app.utils.FrequencyLevel
import com.conscience.app.utils.UsageStatsHelper
import android.util.Log
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
        private const val TAG = "ConscienceSessionManager"

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

    fun recordAnswer(questionText: String, answerText: String, wasAvoidant: Boolean) {
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

        prefs.appendAnswer(questionText, answerText)
    }

    private fun startEscalatingTimer() {
        overlayTimerJob?.cancel()
        overlayTimerJob = scope.launch {
            while (true) {
                val sessionDuration = System.currentTimeMillis() - sessionStartTime
                val dailyDuration = UsageStatsHelper.getTodayUsageMs(
                    context,
                    Constants.INSTAGRAM_PACKAGE
                ) ?: prefs.dailyTimeMs
                val sessionInterval = getIntervalForDuration(sessionDuration)
                val dailyInterval = getIntervalForDailyUsage(dailyDuration)
                val intervalMs = minOf(sessionInterval, dailyInterval)
                Log.d(
                    TAG,
                    "Overlay scheduled in ${intervalMs}ms at session=${sessionDuration}ms daily=${dailyDuration}ms"
                )
                delay(intervalMs)
                Log.d(TAG, "Overlay trigger fired at session=${getSessionDurationMs()}ms")
                onOverlayTrigger?.invoke()
            }
        }
    }

    fun getIntervalForDuration(durationMs: Long): Long {
        return intervalForThresholds(durationMs, Constants.ESCALATION_THRESHOLDS)
    }

    fun getIntervalForDailyUsage(durationMs: Long): Long {
        // User's daily target (20–120 min, enforced by the settings slider)
        val limitMs = (prefs.effectiveDailyLimitMinutes.toLong() * 60 * 1000).coerceAtLeast(20 * 60 * 1000L)

        // Base interval when usage is near zero — depends on how strict the user wants to be
        val baseInterval = when (prefs.frequencyLevel) {
            FrequencyLevel.LOW    -> 10 * 60 * 1000L   // question every 10 min when fresh
            FrequencyLevel.MEDIUM -> 7  * 60 * 1000L   // question every 7 min when fresh
            FrequencyLevel.HIGH   -> 5  * 60 * 1000L   // question every 5 min when fresh
        }

        // At 100% of daily target → 1 min intervals regardless of frequency level
        val minInterval = 60 * 1000L

        // Interpolate: the closer to (or past) the limit, the shorter the interval
        val progress = (durationMs.toDouble() / limitMs).coerceIn(0.0, 1.0)
        val interpolated = baseInterval - ((baseInterval - minInterval) * progress).toLong()
        return interpolated.coerceAtLeast(minInterval)
    }

    private fun intervalForThresholds(durationMs: Long, thresholds: List<Pair<Long, Long>>): Long {
        var selected = thresholds.last().second
        for ((threshold, interval) in thresholds) {
            if (durationMs >= threshold) {
                selected = interval
            }
        }
        return selected
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
