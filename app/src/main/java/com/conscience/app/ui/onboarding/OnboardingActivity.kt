package com.conscience.app.ui.onboarding

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.conscience.app.ui.main.MainActivity

class OnboardingActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val prefs = AppPreferences(applicationContext)

        setContent {
            MaterialTheme(colorScheme = darkColorScheme()) {
                OnboardingScreen(
                    onComplete = {
                        prefs.isOnboardingComplete = true
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    }
                )
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        // Block back on first page — must complete onboarding
    }
}

@Composable
fun OnboardingScreen(onComplete: () -> Unit) {
    var page by remember { mutableStateOf(0) }

    val pages = listOf(
        OnboardingPage(
            emoji = "⚡",
            title = "Take back your time",
            body = "Conscience intercepts every Instagram session with a reality check — before you fall in."
        ),
        OnboardingPage(
            emoji = "🧠",
            title = "Built for awareness",
            body = "Every N minutes you'll face an unskippable question that holds a mirror to your habits.\n\nThe longer you stay, the more frequent it gets."
        ),
        OnboardingPage(
            emoji = "🔒",
            title = "Totally private",
            body = "No internet. No accounts. No data leaves your device — ever.\n\nEverything stays encrypted on your phone."
        ),
        OnboardingPage(
            emoji = "⚙️",
            title = "Two permissions needed",
            body = "Accessibility Service — to detect when Instagram opens.\n\nDraw Over Apps — to show the overlay on top of Instagram.\n\nYou'll set these up after tapping Get Started."
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0A0A0A))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(modifier = Modifier.height(48.dp))

            // Content
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = pages[page].emoji,
                    fontSize = 64.sp
                )
                Spacer(modifier = Modifier.height(32.dp))
                Text(
                    text = pages[page].title,
                    color = Color.White,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Black,
                    textAlign = TextAlign.Center,
                    lineHeight = 34.sp
                )
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = pages[page].body,
                    color = Color(0xFF888888),
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    lineHeight = 26.sp
                )
            }

            // Progress dots
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                pages.indices.forEach { i ->
                    val isActive = i == page
                    Surface(
                        color = if (isActive) Color(0xFFFF3B30) else Color(0xFF333333),
                        shape = RoundedCornerShape(50),
                        modifier = Modifier.size(width = if (isActive) 24.dp else 8.dp, height = 8.dp)
                    ) {}
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // CTA button
            Button(
                onClick = {
                    if (page < pages.size - 1) {
                        page++
                    } else {
                        onComplete()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF3B30))
            ) {
                Text(
                    text = if (page < pages.size - 1) "Next" else "Get Started",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

data class OnboardingPage(
    val emoji: String,
    val title: String,
    val body: String
)
