package com.conscience.app.overlay

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
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
                    sessionManager.recordAnswer(question.text, answer, result.isAvoidant)
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

    androidx.compose.foundation.layout.Box(
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

    androidx.compose.foundation.layout.Box(
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
