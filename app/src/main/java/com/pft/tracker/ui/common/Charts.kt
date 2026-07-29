package com.pft.tracker.ui.common

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.pft.tracker.domain.CategoryBudgetStatus
import com.pft.tracker.ui.theme.BudgetOverColor
import kotlin.math.max

val ChartPalette = listOf(
    Color(0xFFE53935), // Red
    Color(0xFF1E88E5), // Blue
    Color(0xFFFFEB3B), // Yellow
    Color(0xFF8E24AA), // Purple
    Color(0xFFFB8C00), // Orange
    Color(0xFF43A047), // Green
    Color(0xFF00ACC1), // Cyan
    Color(0xFFD81B60), // Pink
    Color(0xFFF4511E), // Deep Orange
    Color(0xFF5E35B1)  // Deep Purple
)

@Composable
fun StackedBudgetBarChart(statuses: List<CategoryBudgetStatus>, modifier: Modifier = Modifier) {
    val nonZeroStatuses = statuses.filter { it.spent > 0 || (it.budget != null && it.budget > 0) }
    
    if (nonZeroStatuses.isEmpty()) {
        Box(modifier = modifier.fillMaxWidth().height(100.dp), contentAlignment = Alignment.Center) {
            Text("ยังไม่มีข้อมูลรายจ่ายที่ต้องแสดง", style = MaterialTheme.typography.bodyMedium)
        }
        return
    }
    
    // Find truly dynamic max value for scale
    val rawMax = nonZeroStatuses.maxOf { max(it.spent, it.budget ?: 0.0) }
    // Calculate a nice maxValue (round up to next 100, 500, 1000, etc.)
    val maxValue = when {
        rawMax <= 0 -> 1000.0
        rawMax < 100 -> 100.0
        rawMax < 500 -> 500.0
        rawMax < 1000 -> 1000.0
        rawMax < 5000 -> ((rawMax / 500).toInt() + 1) * 500.0
        else -> ((rawMax / 1000).toInt() + 1) * 1000.0
    }
    
    val normalColor = MaterialTheme.colorScheme.primary
    val overColor = BudgetOverColor
    val gridColor = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
    val budgetLineColor = Color.Red

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(220.dp)
            .padding(start = 45.dp, top = 20.dp) // Space for Y-axis labels
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Grid Lines
            Canvas(modifier = Modifier.fillMaxSize()) {
                val lines = 5
                for (i in 0..lines) {
                    val y = size.height - (i.toFloat() / lines) * size.height
                    drawLine(
                        color = gridColor,
                        start = Offset(0f, y),
                        end = Offset(size.width, y),
                        strokeWidth = 1f
                    )
                }
            }
            
            // Labels (Y-axis)
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .offset(x = (-45).dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                for (i in 5 downTo 0) {
                    val value = maxValue * i / 5
                    val valText = if (value >= 1000) "${"%.1f".format(value / 1000)}k" else "${value.toInt()}"
                    Text(
                        text = valText,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Bars
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                nonZeroStatuses.forEachIndexed { index, status ->
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(modifier = Modifier.weight(1f)) {
                            Canvas(modifier = Modifier.fillMaxSize().padding(horizontal = 4.dp)) {
                                val withinBudget = if (status.overBudget) (status.budget ?: 0.0) else status.spent
                                val overPart = status.overAmount
                                val withinHeight = (withinBudget / maxValue).toFloat() * size.height
                                val overHeight = (overPart / maxValue).toFloat() * size.height
                                val barWidth = (size.width * 0.8f).coerceAtMost(40.dp.toPx())
                                val startX = (size.width - barWidth) / 2f

                                drawRect(
                                    color = normalColor,
                                    topLeft = Offset(startX, size.height - withinHeight),
                                    size = Size(barWidth, withinHeight)
                                )
                                if (overPart > 0) {
                                    drawRect(
                                        color = overColor,
                                        topLeft = Offset(startX, size.height - withinHeight - overHeight),
                                        size = Size(barWidth, overHeight)
                                    )
                                }
                                val budget = status.budget
                                if (budget != null && budget > 0) {
                                    val budgetY = size.height - (budget / maxValue).toFloat() * size.height
                                    drawLine(
                                        color = budgetLineColor,
                                        start = Offset(0f, budgetY),
                                        end = Offset(size.width, budgetY),
                                        strokeWidth = 2.5f
                                    )
                                }
                            }
                        }
                        Text(
                            text = (index + 1).toString(), // Sequence Number
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            }
        }
    }
}

data class PieSlice(val label: String, val value: Double, val color: Color)

@Composable
fun SimplePieChart(slices: List<PieSlice>, modifier: Modifier = Modifier) {
    val total = slices.sumOf { it.value }
    Row(modifier = modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Canvas(modifier = Modifier.size(140.dp)) {
            if (total <= 0.0) return@Canvas
            var startAngle = -90f
            slices.forEach { slice ->
                val sweep = (slice.value / total * 360.0).toFloat()
                drawArc(
                    color = slice.color,
                    startAngle = startAngle,
                    sweepAngle = sweep,
                    useCenter = true
                )
                startAngle += sweep
            }
        }
        androidx.compose.foundation.layout.Spacer(Modifier.width(16.dp))
        Column {
            slices.forEach { slice ->
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 2.dp)) {
                    Surface(shape = CircleShape, color = slice.color, modifier = Modifier.size(10.dp)) {}
                    val pct = if (total > 0) (slice.value / total * 100).toInt() else 0
                    Text(
                        text = "  ${slice.label} $pct%",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}
