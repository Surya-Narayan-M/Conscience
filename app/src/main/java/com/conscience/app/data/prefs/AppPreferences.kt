package com.conscience.app.data.prefs

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.conscience.app.utils.Constants
import com.conscience.app.utils.FrequencyLevel
import java.time.LocalDate
import org.json.JSONArray
import org.json.JSONObject

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

    var effectiveDailyLimitMinutes: Int
        get() = prefs.getInt(Constants.KEY_EFFECTIVE_DAILY_LIMIT_MIN, 60)
        set(value) = prefs.edit().putInt(Constants.KEY_EFFECTIVE_DAILY_LIMIT_MIN, value).apply()

    var frequencyLevel: FrequencyLevel
        get() {
            val raw = prefs.getString(Constants.KEY_FREQUENCY_LEVEL, FrequencyLevel.MEDIUM.name)
            return runCatching { FrequencyLevel.valueOf(raw ?: FrequencyLevel.MEDIUM.name) }
                .getOrDefault(FrequencyLevel.MEDIUM)
        }
        set(value) = prefs.edit().putString(Constants.KEY_FREQUENCY_LEVEL, value.name).apply()

    var dailySummaryTimeMinutes: Int
        get() {
            val raw = prefs.getInt(Constants.KEY_DAILY_SUMMARY_TIME_MIN, 20 * 60)
            return if (raw < 20 * 60) 20 * 60 else raw
        }
        set(value) {
            val clamped = if (value < 20 * 60) 20 * 60 else value
            prefs.edit().putInt(Constants.KEY_DAILY_SUMMARY_TIME_MIN, clamped).apply()
        }

    private fun resetIfNewDay() {
        val today = LocalDate.now().toString()
        val lastReset = prefs.getString(Constants.KEY_LAST_RESET_DATE, "")
        if (lastReset != today) {
            prefs.edit()
                .putString(Constants.KEY_LAST_RESET_DATE, today)
                .putInt(Constants.KEY_DAILY_OPEN_COUNT, 0)
                .putLong(Constants.KEY_DAILY_TIME_MS, 0L)
                .putString(Constants.KEY_ANSWERS_DATE, today)
                .putString(Constants.KEY_TODAY_ANSWERS_JSON, "[]")
                .apply()
        }
    }

    fun appendAnswer(questionText: String, answerText: String) {
        resetIfNewDay()
        val current = prefs.getString(Constants.KEY_TODAY_ANSWERS_JSON, "[]") ?: "[]"
        val array = JSONArray(current)
        val entry = JSONObject()
        entry.put("question", questionText)
        entry.put("answer", answerText)
        array.put(entry)
        prefs.edit().putString(Constants.KEY_TODAY_ANSWERS_JSON, array.toString()).apply()
    }

    fun getTodayAnswers(): List<Pair<String, String>> {
        resetIfNewDay()
        val current = prefs.getString(Constants.KEY_TODAY_ANSWERS_JSON, "[]") ?: "[]"
        val array = JSONArray(current)
        val result = ArrayList<Pair<String, String>>(array.length())
        for (i in 0 until array.length()) {
            val obj = array.getJSONObject(i)
            result.add(obj.optString("question") to obj.optString("answer"))
        }
        return result
    }

    fun clearTodayAnswers() {
        prefs.edit().putString(Constants.KEY_TODAY_ANSWERS_JSON, "[]").apply()
    }

    fun clearAll() {
        prefs.edit().clear().apply()
    }

    fun incrementDailyOpenCount() {
        dailyOpenCount = dailyOpenCount + 1
        totalOpensAllTime = totalOpensAllTime + 1
    }
}
