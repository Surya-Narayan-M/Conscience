package com.conscience.app.ui.settings

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.conscience.app.data.prefs.AppPreferences
import com.conscience.app.notifications.NotificationScheduler
import com.conscience.app.security.DataSecurityManager
import com.conscience.app.utils.FrequencyLevel

class SettingsActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val prefs = AppPreferences(applicationContext)

        setContent {
            MaterialTheme(colorScheme = darkColorScheme()) {
                SettingsScreen(
                    prefs = prefs,
                    onOpenAccessibilitySettings = {
                        startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
                    },
                    onOpenOverlaySettings = {
                        startActivity(
                            Intent(
                                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                                Uri.parse("package:$packageName")
                            )
                        )
                    },
                    onRescheduleSummary = {
                        NotificationScheduler.scheduleDailySummary(applicationContext)
                    },
                    onWipeData = {
                        DataSecurityManager.wipeAllData(applicationContext)
                    },
                    onBack = { finish() }
                )
            }
        }
    }
}

@Composable
fun SettingsScreen(
    prefs: AppPreferences,
    onOpenAccessibilitySettings: () -> Unit,
    onOpenOverlaySettings: () -> Unit,
    onRescheduleSummary: () -> Unit,
    onWipeData: () -> Unit,
    onBack: () -> Unit
) {
    var isEnabled by remember { mutableStateOf(prefs.isAppEnabled) }
    var frequencyLevel by remember { mutableStateOf(prefs.frequencyLevel) }
    var dailyLimitMinutes by remember { mutableStateOf(prefs.effectiveDailyLimitMinutes) }
    var summaryHour by remember { mutableStateOf(prefs.dailySummaryTimeMinutes / 60) }
    var showWipeConfirm by remember { mutableStateOf(false) }

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
                .padding(top = 56.dp, bottom = 32.dp)
        ) {
            // Header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Settings",
                    color = Color.White,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Black,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // === SECTION: App Control ===
            SectionLabel("APP CONTROL")
            SettingsCard {
                SettingsRow(
                    title = "Conscience Active",
                    subtitle = "Intercept Instagram opens"
                ) {
                    Switch(
                        checked = isEnabled,
                        onCheckedChange = {
                            isEnabled = it
                            prefs.isAppEnabled = it
                        },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = Color(0xFFFF3B30)
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // === SECTION: Intervention Frequency ===
            SectionLabel("INTERVENTION FREQUENCY")
            SettingsCard {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "How aggressive should interventions be?",
                        color = Color(0xFF888888),
                        fontSize = 12.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    FrequencyLevel.values().forEach { level ->
                        val isSelected = frequencyLevel == level
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = isSelected,
                                onClick = {
                                    frequencyLevel = level
                                    prefs.frequencyLevel = level
                                },
                                colors = RadioButtonDefaults.colors(
                                    selectedColor = Color(0xFFFF3B30),
                                    unselectedColor = Color(0xFF555555)
                                )
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = level.label,
                                    color = if (isSelected) Color.White else Color(0xFF888888),
                                    fontSize = 14.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                                Text(
                                    text = when (level) {
                                        FrequencyLevel.LOW -> "Less frequent check-ins (relaxed)"
                                        FrequencyLevel.MEDIUM -> "Balanced — recommended"
                                        FrequencyLevel.HIGH -> "Aggressive — maximum friction"
                                    },
                                    color = Color(0xFF555555),
                                    fontSize = 11.sp
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // === SECTION: Daily Limit ===
            SectionLabel("DAILY USAGE TARGET")
            SettingsCard {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "How long do you want to use Instagram per day?",
                        color = Color(0xFF888888),
                        fontSize = 12.sp
                    )
                    Text(
                        text = "Questions become more frequent as you approach this limit.",
                        color = Color(0xFF444444),
                        fontSize = 11.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Daily target",
                            color = Color(0xFF888888),
                            fontSize = 14.sp
                        )
                        Text(
                            text = if (dailyLimitMinutes < 60) "$dailyLimitMinutes min"
                                   else "${dailyLimitMinutes / 60}h ${dailyLimitMinutes % 60}m".trimEnd('m').trimEnd(' ').let {
                                       if (dailyLimitMinutes % 60 == 0) "${dailyLimitMinutes / 60}h" else "${dailyLimitMinutes / 60}h ${dailyLimitMinutes % 60}m"
                                   },
                            color = Color(0xFFFF3B30),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Slider(
                        value = dailyLimitMinutes.toFloat(),
                        onValueChange = {
                            dailyLimitMinutes = it.toInt()
                        },
                        onValueChangeFinished = {
                            prefs.effectiveDailyLimitMinutes = dailyLimitMinutes
                        },
                        valueRange = 20f..120f,
                        steps = 9,
                        colors = SliderDefaults.colors(
                            thumbColor = Color(0xFFFF3B30),
                            activeTrackColor = Color(0xFFFF3B30),
                            inactiveTrackColor = Color(0xFF333333)
                        )
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("20 min", color = Color(0xFF444444), fontSize = 10.sp)
                        Text("2 hours", color = Color(0xFF444444), fontSize = 10.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // === SECTION: Daily Summary Time ===
            SectionLabel("DAILY SUMMARY NOTIFICATION")
            SettingsCard {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Send summary at",
                            color = Color(0xFF888888),
                            fontSize = 14.sp
                        )
                        Text(
                            text = "${summaryHour}:00",
                            color = Color(0xFFFF3B30),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Slider(
                        value = summaryHour.toFloat(),
                        onValueChange = {
                            summaryHour = it.toInt()
                        },
                        onValueChangeFinished = {
                            prefs.dailySummaryTimeMinutes = summaryHour * 60
                            onRescheduleSummary()
                        },
                        valueRange = 20f..23f,
                        steps = 2,
                        colors = SliderDefaults.colors(
                            thumbColor = Color(0xFFFF3B30),
                            activeTrackColor = Color(0xFFFF3B30),
                            inactiveTrackColor = Color(0xFF333333)
                        )
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("8 PM", color = Color(0xFF444444), fontSize = 10.sp)
                        Text("11 PM", color = Color(0xFF444444), fontSize = 10.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // === SECTION: Permissions ===
            SectionLabel("PERMISSIONS")
            SettingsCard {
                Column(modifier = Modifier.padding(8.dp)) {
                    SettingsButton(
                        text = "Accessibility Settings",
                        subtitle = "Required to detect Instagram"
                    ) { onOpenAccessibilitySettings() }
                    Divider(color = Color(0xFF1E1E1E))
                    SettingsButton(
                        text = "Draw Over Apps",
                        subtitle = "Required for overlays to appear"
                    ) { onOpenOverlaySettings() }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // === SECTION: Data ===
            SectionLabel("DATA")
            SettingsCard {
                Column(modifier = Modifier.padding(8.dp)) {
                    SettingsButton(
                        text = "Erase All Data",
                        subtitle = "Delete all sessions and preferences",
                        isDestructive = true
                    ) { showWipeConfirm = true }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "No internet. No accounts. All data stays on your device.",
                color = Color(0xFF333333),
                fontSize = 11.sp,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }

    // Wipe confirmation dialog
    if (showWipeConfirm) {
        AlertDialog(
            onDismissRequest = { showWipeConfirm = false },
            containerColor = Color(0xFF1A0A0A),
            title = {
                Text("Erase All Data?", color = Color.White, fontWeight = FontWeight.Bold)
            },
            text = {
                Text(
                    "This will permanently delete all your sessions and settings. This cannot be undone.",
                    color = Color(0xFF888888),
                    fontSize = 14.sp
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    showWipeConfirm = false
                    onWipeData()
                }) {
                    Text("Erase", color = Color(0xFFFF3B30), fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showWipeConfirm = false }) {
                    Text("Cancel", color = Color(0xFF888888))
                }
            }
        )
    }
}

@Composable
fun SectionLabel(text: String) {
    Text(
        text = text,
        color = Color(0xFF444444),
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 1.5.sp,
        modifier = Modifier.padding(bottom = 8.dp)
    )
}

@Composable
fun SettingsCard(content: @Composable () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color(0xFF141414),
        shape = RoundedCornerShape(14.dp)
    ) {
        content()
    }
}

@Composable
fun SettingsRow(
    title: String,
    subtitle: String,
    trailing: @Composable () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f).padding(end = 16.dp)) {
            Text(title, color = Color.White, fontSize = 14.sp)
            Text(subtitle, color = Color(0xFF666666), fontSize = 12.sp)
        }
        trailing()
    }
}

@Composable
fun SettingsButton(
    text: String,
    subtitle: String,
    isDestructive: Boolean = false,
    onClick: () -> Unit
) {
    TextButton(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = text,
                color = if (isDestructive) Color(0xFFFF3B30) else Color.White,
                fontSize = 14.sp
            )
            Text(
                text = subtitle,
                color = Color(0xFF555555),
                fontSize = 11.sp
            )
        }
        Text(
            text = "›",
            color = Color(0xFF555555),
            fontSize = 20.sp
        )
    }
}
