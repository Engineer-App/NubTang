package com.pft.tracker.ui.nav

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pft.tracker.domain.model.TransactionType

data class TransactionTypeOption(
    val type: TransactionType,
    val label: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)

val transactionTypeOptions = listOf(
    TransactionTypeOption(TransactionType.EXPENSE, "รายจ่าย", Icons.Filled.TrendingDown),
    TransactionTypeOption(TransactionType.INCOME, "รายรับ", Icons.Filled.TrendingUp),
    TransactionTypeOption(TransactionType.TRANSFER, "โอนเงิน", Icons.Filled.SwapHoriz),
    TransactionTypeOption(TransactionType.CASH_WITHDRAWAL, "ถอนเงินสด", Icons.Filled.AccountBalanceWallet),
    TransactionTypeOption(TransactionType.CREDIT_CARD_PAYMENT, "ชำระบัตรเครดิต", Icons.Filled.CreditCard)
)

@Composable
fun TransactionTypePickerContent(onSelect: (TransactionType) -> Unit) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "เลือกประเภทธุรกรรม",
            style = MaterialTheme.typography.titleMedium
        )
        androidx.compose.foundation.layout.Spacer(Modifier.size(12.dp))
        transactionTypeOptions.forEach { option ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onSelect(option.type) }
                    .padding(vertical = 12.dp),
            ) {
                androidx.compose.foundation.layout.Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Surface(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.primaryContainer,
                        modifier = Modifier.size(40.dp)
                    ) {
                        androidx.compose.foundation.layout.Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth()) {
                            Icon(option.icon, contentDescription = null, tint = MaterialTheme.colorScheme.onPrimaryContainer)
                        }
                    }
                    Text(option.label, style = MaterialTheme.typography.bodyLarge)
                }
            }
        }
    }
}
