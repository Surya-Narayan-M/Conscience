package com.conscience.app.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface SessionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(session: SessionEntity): Long

    @Update
    suspend fun updateSession(session: SessionEntity)

    @Query("SELECT * FROM sessions WHERE dateKey = :dateKey ORDER BY startTimeMs DESC")
    fun getSessionsByDate(dateKey: String): Flow<List<SessionEntity>>

    @Query("SELECT * FROM sessions WHERE startTimeMs BETWEEN :startMs AND :endMs ORDER BY startTimeMs ASC")
    fun getSessionsBetween(startMs: Long, endMs: Long): Flow<List<SessionEntity>>

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
