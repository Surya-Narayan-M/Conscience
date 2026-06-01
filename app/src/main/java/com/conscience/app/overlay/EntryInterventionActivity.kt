package com.conscience.app.overlay

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.conscience.app.questions.QuestionBank
import com.conscience.app.session.SessionManager

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
                    sessionManager.getSessionDurationMs()
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
                    text = "Today: $dailyOpens opens",
                    color = Color(0xFF8E8E93),
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "This session: $sessionDurationFormatted",
                    color = Color(0xFF8E8E93),
                    fontSize = 14.sp
                )
            }

            // Main message
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = message,
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center,
                    lineHeight = 30.sp
                )
            }

            // Countdown
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = if (canDismiss) "Proceed" else "Wait $timeRemaining",
                    color = Color(0xFFFF3B30),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(48.dp))
            }
        }
    }
}
