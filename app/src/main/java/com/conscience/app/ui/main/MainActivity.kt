package com.conscience.app.ui.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.conscience.app.data.prefs.AppPreferences
import com.conscience.app.notifications.NotificationScheduler
import com.conscience.app.service.ConscienceForegroundService
import com.conscience.app.stats.StatsEngine
import com.conscience.app.ui.onboarding.OnboardingActivity
import com.conscience.app.ui.settings.SettingsActivity
import com.conscience.app.ui.stats.StatsActivity

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

        // Reschedule daily summary on each launch (idempotent)
        NotificationScheduler.scheduleDailySummary(applicationContext)

        val stats = StatsEngine(applicationContext)

        setContent {
            MaterialTheme(colorScheme = darkColorScheme()) {
                val todayStats by stats.getTodayStats().collectAsState(initial = null)
                val isAccessibilityEnabled = isAccessibilityServiceEnabled()
                val isOverlayEnabled = Settings.canDrawOverlays(this)

                MainScreen(
                    todayOpens = todayStats?.openCount ?: 0,
                    todayTime = stats.formatTimeMs(todayStats?.totalTimeMs ?: 0L),
                    questionsAnswered = todayStats?.questionsAnswered ?: 0,
                    avoidantAnswers = todayStats?.avoidantAnswers ?: 0,
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
                    },
                    onOpenSettings = {
                        startActivity(Intent(this, SettingsActivity::class.java))
                    },
                    onOpenStats = {
                        startActivity(Intent(this, StatsActivity::class.java))
                    }
                )
            }
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
    avoidantAnswers: Int,
    isAccessibilityEnabled: Boolean,
    isOverlayEnabled: Boolean,
    onEnableAccessibility: () -> Unit,
    onEnableOverlay: () -> Unit,
    onOpenSettings: () -> Unit,
    onOpenStats: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0A0A0A))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
                .padding(top = 56.dp, bottom = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header
            Text(
                text = "CONSCIENCE",
                color = Color.White,
                fontSize = 28.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 4.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Today's Instagram Report",
                color = Color(0xFF666666),
                fontSize = 13.sp,
                letterSpacing = 1.sp
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Status indicator
            val isFullyActive = isAccessibilityEnabled && isOverlayEnabled
            Surface(
                color = if (isFullyActive) Color(0xFF0D2B0D) else Color(0xFF2B0D0D),
                shape = RoundedCornerShape(20.dp)
            ) {
                Text(
                    text = if (isFullyActive) "● ACTIVE — Monitoring Instagram" else "⚠ SETUP REQUIRED",
                    color = if (isFullyActive) Color(0xFF4CAF50) else Color(0xFFFF3B30),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Stats grid
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard(
                    label = "Opens",
                    value = todayOpens.toString(),
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    label = "Time",
                    value = todayTime,
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard(
                    label = "Questions",
                    value = questionsAnswered.toString(),
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    label = "Avoidant",
                    value = avoidantAnswers.toString(),
                    valueColor = if (avoidantAnswers > 0) Color(0xFFFF3B30) else null,
                    modifier = Modifier.weight(1f)
                )
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
                    message = "Draw over apps permission missing — overlays won't appear",
                    buttonText = "Grant Permission",
                    onClick = onEnableOverlay
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            Spacer(modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.height(24.dp))

            // Bottom row: Stats + Settings
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onOpenStats,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFFF3B30)),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF2A1010))
                ) {
                    Text("📊  Stats", fontSize = 14.sp)
                }
                OutlinedButton(
                    onClick = onOpenSettings,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF888888)),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF333333))
                ) {
                    Text("Settings", fontSize = 14.sp)
                }
            }
        }
    }
}

@Composable
fun StatCard(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    valueColor: Color? = null
) {
    Surface(
        modifier = modifier,
        color = Color(0xFF141414),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value,
                color = valueColor ?: Color.White,
                fontSize = 32.sp,
                fontWeight = FontWeight.Black,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = label.uppercase(),
                color = Color(0xFF555555),
                fontSize = 10.sp,
                letterSpacing = 1.5.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun WarningCard(message: String, buttonText: String, onClick: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color(0xFF1A0D0D),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = message,
                color = Color(0xFFFF6B6B),
                fontSize = 13.sp,
                lineHeight = 20.sp
            )
            Spacer(modifier = Modifier.height(12.dp))
            Button(
                onClick = onClick,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF3B30)),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(buttonText, fontSize = 13.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
