package com.conscience.app.stats

import android.content.Context
import com.conscience.app.data.db.AppDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import java.time.YearMonth
import java.time.ZoneId

data class DailyStats(
    val date: String,
    val openCount: Int,
    val totalTimeMs: Long,
    val questionsAnswered: Int,
    val avoidantAnswers: Int
)

data class DailyUsage(
    val date: String,
    val totalMinutes: Int,
    val openCount: Int
)

class StatsEngine(context: Context) {

    private val db = AppDatabase.getInstance(context)

    /** Live-updating today's stats — used by the dashboard. */
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

    /** Monthly calendar data — used by the monthly graph UI. */
    fun getMonthlyUsage(year: Int, month: Int): Flow<List<DailyUsage>> {
        val yearMonth = YearMonth.of(year, month)
        val start = yearMonth.atDay(1)
        val end = yearMonth.atEndOfMonth()
        val startMs = start.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
        val endMs = end.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli() - 1

        return db.sessionDao().getSessionsBetween(startMs, endMs).map { sessions ->
            val dayMap = LinkedHashMap<String, DailyUsage>()
            var cursor = start
            while (!cursor.isAfter(end)) {
                val key = cursor.toString()
                dayMap[key] = DailyUsage(key, 0, 0)
                cursor = cursor.plusDays(1)
            }

            sessions.groupBy { it.dateKey }.forEach { (dateKey, daySessions) ->
                val totalMinutes = (daySessions.sumOf { it.durationMs } / 60000L).toInt()
                val openCount = daySessions.size
                if (dayMap.containsKey(dateKey)) {
                    dayMap[dateKey] = DailyUsage(dateKey, totalMinutes, openCount)
                }
            }

            dayMap.values.toList()
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
}
