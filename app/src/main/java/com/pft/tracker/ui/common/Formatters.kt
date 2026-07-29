package com.pft.tracker.ui.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.text.NumberFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

private val currencyFormat = NumberFormat.getNumberInstance(Locale("th", "TH")).apply {
    minimumFractionDigits = 2
    maximumFractionDigits = 2
}

fun formatBaht(amount: Double): String = "${currencyFormat.format(amount)} ฿"

fun formatBahtSigned(amount: Double): String {
    val sign = if (amount > 0) "+" else ""
    return "$sign${formatBaht(amount)}"
}

private val dateFormatter = DateTimeFormatter.ofPattern("d MMM yyyy", Locale("th", "TH"))
private val dateTimeFormatter = DateTimeFormatter.ofPattern("d MMM yyyy HH:mm", Locale("th", "TH"))

fun formatDate(date: LocalDate): String = date.format(dateFormatter)

fun monthLabel(year: Int, month: Int): String {
    val date = LocalDate.of(year, month, 1)
    return "${date.month.getDisplayName(TextStyle.FULL, Locale("th", "TH"))} ${year + 543}"
}

@Composable
fun SummaryFooter(income: Double, expense: Double) {
    Surface(
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f),
        tonalElevation = 8.dp,
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text("รายรับรวม", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(formatBaht(income), style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
            }
            Column(horizontalAlignment = androidx.compose.ui.Alignment.End) {
                Text("รายจ่ายรวม", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(formatBaht(expense), style = MaterialTheme.typography.titleMedium, color = com.pft.tracker.ui.theme.BudgetOverColor, fontWeight = FontWeight.Bold)
            }
        }
    }
}
