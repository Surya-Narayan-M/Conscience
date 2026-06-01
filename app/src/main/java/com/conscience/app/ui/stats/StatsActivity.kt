package com.conscience.app.ui.stats

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.conscience.app.data.prefs.AppPreferences
import com.conscience.app.stats.DailyUsage
import com.conscience.app.stats.StatsEngine
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

class StatsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val stats = StatsEngine(applicationContext)
        val prefs = AppPreferences(applicationContext)

        setContent {
            MaterialTheme(colorScheme = darkColorScheme()) {
                StatsScreen(
                    stats = stats,
                    dailyLimitMinutes = prefs.effectiveDailyLimitMinutes,
                    onBack = { finish() }
                )
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Main screen
// ─────────────────────────────────────────────────────────────────────────────

@Composable
fun StatsScreen(
    stats: StatsEngine,
    dailyLimitMinutes: Int,
    onBack: () -> Unit
) {
    val today = LocalDate.now()
    var currentMonth by remember { mutableStateOf(YearMonth.of(today.year, today.month)) }

    val monthlyData by stats.getMonthlyUsage(
        currentMonth.year,
        currentMonth.monthValue
    ).collectAsState(initial = emptyList())

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0A0A0A))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
                .padding(top = 52.dp, bottom = 32.dp)
        ) {

            // ── Header ──────────────────────────────────────────────
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "STATS",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 3.sp,
                    modifier = Modifier.weight(1f)
                )
                TextButton(onClick = onBack) {
                    Text("Done", color = Color(0xFFFF3B30), fontSize = 15.sp)
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // ── Month navigator ──────────────────────────────────────
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(onClick = { currentMonth = currentMonth.minusMonths(1) }) {
                    Text("‹", color = Color(0xFFFF3B30), fontSize = 32.sp, fontWeight = FontWeight.Thin)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = currentMonth.month.getDisplayName(TextStyle.FULL, Locale.getDefault()),
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${currentMonth.year}",
                        color = Color(0xFF555555),
                        fontSize = 12.sp
                    )
                }
                val canGoNext = currentMonth.isBefore(YearMonth.now())
                TextButton(
                    onClick = { if (canGoNext) currentMonth = currentMonth.plusMonths(1) },
                    enabled = canGoNext
                ) {
                    Text(
                        "›",
                        color = if (canGoNext) Color(0xFFFF3B30) else Color(0xFF2A2A2A),
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Thin
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ── Summary cards ────────────────────────────────────────
            val totalMinutes  = monthlyData.sumOf { it.totalMinutes }
            val totalOpens    = monthlyData.sumOf { it.openCount }
            val daysWithUsage = monthlyData.count { it.totalMinutes > 0 }
            val avgMinutes    = if (daysWithUsage > 0) totalMinutes / daysWithUsage else 0

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                SummaryCard("TOTAL TIME",  formatMinutes(totalMinutes), Modifier.weight(1f))
                SummaryCard("DAILY AVG",   formatMinutes(avgMinutes),   Modifier.weight(1f), avgMinutes > dailyLimitMinutes)
                SummaryCard("OPENS",       "$totalOpens",               Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(28.dp))

            // ── Section title ────────────────────────────────────────
            Text(
                "MINUTES PER DAY",
                color = Color(0xFF444444), fontSize = 11.sp,
                fontWeight = FontWeight.Bold, letterSpacing = 1.5.sp
            )
            Spacer(modifier = Modifier.height(8.dp))

            // ── Legend ───────────────────────────────────────────────
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                LegendItem(Color(0xFFFF3B30), "Usage")
                LegendItem(Color(0xFF555555), "Target (${dailyLimitMinutes}m)", dashed = true)
                LegendItem(Color(0xFFFFD60A), "Today")
            }
            Spacer(modifier = Modifier.height(12.dp))

            // ── Minutes chart ────────────────────────────────────────
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color(0xFF111111),
                shape = RoundedCornerShape(16.dp)
            ) {
                if (monthlyData.isEmpty()) {
                    EmptyChart(200.dp)
                } else {
                    MinutesChart(monthlyData, dailyLimitMinutes, today, currentMonth)
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // ── Opens chart ──────────────────────────────────────────
            Text(
                "OPENS PER DAY",
                color = Color(0xFF444444), fontSize = 11.sp,
                fontWeight = FontWeight.Bold, letterSpacing = 1.5.sp
            )
            Spacer(modifier = Modifier.height(12.dp))

            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color(0xFF111111),
                shape = RoundedCornerShape(16.dp)
            ) {
                if (monthlyData.isEmpty()) {
                    EmptyChart(140.dp)
                } else {
                    OpensChart(monthlyData, today, currentMonth)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                "Data older than 30 days is automatically deleted.",
                color = Color(0xFF2A2A2A), fontSize = 11.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Minutes per day chart
// ─────────────────────────────────────────────────────────────────────────────

@Composable
fun MinutesChart(
    data: List<DailyUsage>,
    dailyLimitMinutes: Int,
    today: LocalDate,
    currentMonth: YearMonth
) {
    val barW   = 14.dp
    val barGap = 6.dp
    val slot   = barW + barGap
    val chartH = 180.dp
    val xAxisH = 20.dp
    val yAxisW = 40.dp

    val maxMinutes = data.maxOfOrNull { it.totalMinutes }
        ?.coerceAtLeast(dailyLimitMinutes + 5) ?: (dailyLimitMinutes + 5)

    Row(
        modifier = Modifier.padding(
            start = 12.dp, end = 12.dp, top = 16.dp, bottom = 12.dp
        )
    ) {
        // Y-axis labels (3 ticks: 0, mid, max)
        Box(modifier = Modifier.width(yAxisW).height(chartH + xAxisH)) {
            listOf(0, maxMinutes / 2, maxMinutes).forEach { value ->
                val frac = value.toFloat() / maxMinutes          // 0.0 → 1.0
                val topOffset = chartH * (1f - frac)             // 0 = top of bar area
                Box(
                    modifier = Modifier
                        .offset(y = topOffset)
                        .width(yAxisW)
                ) {
                    Text(
                        text = formatMinutesShort(value),
                        color = Color(0xFF555555),
                        fontSize = 9.sp,
                        modifier = Modifier.align(Alignment.CenterStart)
                    )
                }
            }
        }

        // Scrollable bar + x-label area
        val scrollState = rememberScrollState()
        Column(
            modifier = Modifier
                .weight(1f)
                .horizontalScroll(scrollState)
        ) {
            // The bar canvas
            Box(modifier = Modifier.width(slot * data.size).height(chartH)) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val bw = barW.toPx()
                    val bg = barGap.toPx()
                    val limitFrac = dailyLimitMinutes.toFloat() / maxMinutes
                    val limitY   = size.height * (1f - limitFrac)

                    // Dashed limit line
                    drawLine(
                        color = Color(0xFF444444),
                        start = Offset(0f, limitY),
                        end   = Offset(size.width, limitY),
                        strokeWidth = 1.dp.toPx(),
                        pathEffect  = PathEffect.dashPathEffect(floatArrayOf(6f, 6f))
                    )

                    data.forEachIndexed { i, day ->
                        val x       = i * (bw + bg)
                        val isToday = currentMonth.year       == today.year &&
                                      currentMonth.monthValue == today.monthValue &&
                                      (i + 1)                == today.dayOfMonth
                        val isFuture = currentMonth == YearMonth.now() && (i + 1) > today.dayOfMonth

                        if (day.totalMinutes == 0 || isFuture) {
                            drawRoundRect(
                                color      = Color(0xFF1E1E1E),
                                topLeft    = Offset(x, size.height - 4.dp.toPx()),
                                size       = Size(bw, 4.dp.toPx()),
                                cornerRadius = CornerRadius(2.dp.toPx())
                            )
                        } else {
                            val barH    = (day.totalMinutes.toFloat() / maxMinutes) * size.height
                            val exceeded = day.totalMinutes > dailyLimitMinutes
                            drawRoundRect(
                                color      = if (exceeded) Color(0xFFFF6B35) else Color(0xFFFF3B30),
                                topLeft    = Offset(x, size.height - barH),
                                size       = Size(bw, barH),
                                cornerRadius = CornerRadius(3.dp.toPx(), 3.dp.toPx())
                            )
                        }

                        // Today dot
                        if (isToday) {
                            drawCircle(
                                color  = Color(0xFFFFD60A),
                                radius = 3.dp.toPx(),
                                center = Offset(x + bw / 2f, size.height - 6.dp.toPx())
                            )
                        }
                    }
                }
            }

            // X-axis labels in a Row
            Row(modifier = Modifier.width(slot * data.size).height(xAxisH)) {
                data.forEachIndexed { i, _ ->
                    val dayNum = i + 1
                    val isToday = currentMonth.year       == today.year &&
                                  currentMonth.monthValue == today.monthValue &&
                                  dayNum                  == today.dayOfMonth
                    Box(modifier = Modifier.width(slot).height(xAxisH)) {
                        if (dayNum == 1 || dayNum % 5 == 0) {
                            Text(
                                text  = "$dayNum",
                                color = if (isToday) Color(0xFFFFD60A) else Color(0xFF555555),
                                fontSize = 9.sp,
                                textAlign = TextAlign.Center,
                                modifier  = Modifier.fillMaxWidth().align(Alignment.Center)
                            )
                        }
                    }
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Opens per day chart
// ─────────────────────────────────────────────────────────────────────────────

@Composable
fun OpensChart(
    data: List<DailyUsage>,
    today: LocalDate,
    currentMonth: YearMonth
) {
    val barW   = 14.dp
    val barGap = 6.dp
    val slot   = barW + barGap
    val chartH = 100.dp
    val xAxisH = 20.dp

    val maxOpens = data.maxOfOrNull { it.openCount }?.coerceAtLeast(1) ?: 1

    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 12.dp, end = 12.dp, top = 12.dp, bottom = 12.dp)
            .horizontalScroll(scrollState)
    ) {
        Box(modifier = Modifier.width(slot * data.size).height(chartH)) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val bw = barW.toPx()
                val bg = barGap.toPx()

                data.forEachIndexed { i, day ->
                    val x       = i * (bw + bg)
                    val isToday = currentMonth.year       == today.year &&
                                  currentMonth.monthValue == today.monthValue &&
                                  (i + 1)                == today.dayOfMonth
                    val isFuture = currentMonth == YearMonth.now() && (i + 1) > today.dayOfMonth

                    if (day.openCount == 0 || isFuture) {
                        drawRoundRect(
                            color      = Color(0xFF1A1A1A),
                            topLeft    = Offset(x, size.height - 3.dp.toPx()),
                            size       = Size(bw, 3.dp.toPx()),
                            cornerRadius = CornerRadius(2.dp.toPx())
                        )
                    } else {
                        val barH = (day.openCount.toFloat() / maxOpens) * size.height
                        drawRoundRect(
                            color      = Color(0xFF2A5298),
                            topLeft    = Offset(x, size.height - barH),
                            size       = Size(bw, barH),
                            cornerRadius = CornerRadius(3.dp.toPx(), 3.dp.toPx())
                        )
                    }
                    if (isToday) {
                        drawCircle(
                            color  = Color(0xFFFFD60A),
                            radius = 3.dp.toPx(),
                            center = Offset(x + bw / 2f, size.height - 6.dp.toPx())
                        )
                    }
                }
            }

            // Count labels above bars as Text overlays
            data.forEachIndexed { i, day ->
                if (day.openCount > 0) {
                    val barH = (day.openCount.toFloat() / maxOpens)
                    Box(
                        modifier = Modifier
                            .offset(x = slot * i)
                            .width(slot)
                            .fillMaxHeight(1f - barH)
                    ) {
                        Text(
                            text  = "${day.openCount}",
                            color = Color(0xFF888888),
                            fontSize = 8.sp,
                            textAlign = TextAlign.Center,
                            modifier  = Modifier.fillMaxWidth().align(Alignment.BottomCenter)
                        )
                    }
                }
            }
        }

        // X-axis labels
        Row(modifier = Modifier.width(slot * data.size).height(xAxisH)) {
            data.forEachIndexed { i, _ ->
                val dayNum  = i + 1
                val isToday = currentMonth.year       == today.year &&
                              currentMonth.monthValue == today.monthValue &&
                              dayNum                  == today.dayOfMonth
                Box(modifier = Modifier.width(slot).height(xAxisH)) {
                    if (dayNum == 1 || dayNum % 5 == 0) {
                        Text(
                            text  = "$dayNum",
                            color = if (isToday) Color(0xFFFFD60A) else Color(0xFF555555),
                            fontSize = 9.sp,
                            textAlign = TextAlign.Center,
                            modifier  = Modifier.fillMaxWidth().align(Alignment.Center)
                        )
                    }
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Small UI helpers
// ─────────────────────────────────────────────────────────────────────────────

@Composable
fun EmptyChart(height: Dp) {
    Box(
        modifier = Modifier.fillMaxWidth().height(height),
        contentAlignment = Alignment.Center
    ) {
        Text("No data for this month", color = Color(0xFF333333), fontSize = 13.sp)
    }
}

@Composable
fun SummaryCard(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    highlight: Boolean = false
) {
    Surface(modifier = modifier, color = Color(0xFF141414), shape = RoundedCornerShape(14.dp)) {
        Column(
            modifier = Modifier.padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text  = value,
                color = if (highlight) Color(0xFFFF3B30) else Color.White,
                fontSize = 22.sp, fontWeight = FontWeight.Black,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(3.dp))
            Text(
                text  = label,
                color = Color(0xFF444444),
                fontSize = 9.sp, letterSpacing = 1.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun LegendItem(color: Color, label: String, dashed: Boolean = false) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(width = 14.dp, height = 3.dp)
                .background(color, RoundedCornerShape(2.dp))
        )
        Spacer(modifier = Modifier.width(5.dp))
        Text(label, color = Color(0xFF555555), fontSize = 10.sp)
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Formatting helpers
// ─────────────────────────────────────────────────────────────────────────────

private fun formatMinutes(m: Int): String {
    if (m <= 0) return "0m"
    val h = m / 60; val min = m % 60
    return when { h > 0 && min > 0 -> "${h}h ${min}m"; h > 0 -> "${h}h"; else -> "${min}m" }
}

private fun formatMinutesShort(m: Int): String {
    if (m == 0) return "0"
    val h = m / 60; val min = m % 60
    return if (h > 0) "${h}h" else "${min}m"
}
