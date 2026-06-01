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
