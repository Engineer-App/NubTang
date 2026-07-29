package com.pft.tracker.ui.creditcards

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.pft.tracker.di.AppContainer
import com.pft.tracker.di.ViewModelFactory
import com.pft.tracker.domain.StatementSummary
import com.pft.tracker.domain.model.StatementStatus
import com.pft.tracker.ui.common.formatBaht
import com.pft.tracker.ui.common.formatDate
import com.pft.tracker.ui.nav.Routes
import com.pft.tracker.ui.theme.BudgetOverColor

@Composable
fun CreditCardDetailScreen(container: AppContainer, navController: NavController, cardId: Long) {
    val viewModel: CreditCardDetailViewModel = viewModel(factory = ViewModelFactory { CreditCardDetailViewModel(container, cardId) })
    val state by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(state.card?.name ?: "") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "ย้อนกลับ")
                    }
                },
                actions = {
                    IconButton(onClick = { navController.navigate(Routes.creditCardEdit(cardId)) }) {
                        Icon(Icons.Filled.Edit, contentDescription = "แก้ไข")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(16.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Column {
                                Text("ใช้ไป", style = MaterialTheme.typography.labelMedium)
                                Text(formatBaht(state.currentUsed), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                            }
                            Column {
                                Text("วงเงินคงเหลือ", style = MaterialTheme.typography.labelMedium)
                                Text(formatBaht(state.availableLimit), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
            item {
                Text("รอบบิล", style = MaterialTheme.typography.titleMedium)
            }
            items(state.statements) { statement ->
                StatementCard(statement)
            }
        }
    }
}

@Composable
private fun StatementCard(statement: StatementSummary) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(12.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(
                    "${formatDate(statement.periodStart)} - ${formatDate(statement.periodEnd)}",
                    style = MaterialTheme.typography.bodyMedium
                )
                StatusBadge(statement.status)
            }
            Text("ยอดรวม ${formatBaht(statement.statementAmount)}  •  ชำระแล้ว ${formatBaht(statement.paidAmount)}", style = MaterialTheme.typography.bodySmall)
            Text("ครบกำหนดชำระ ${formatDate(statement.dueDate)}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun StatusBadge(status: StatementStatus) {
    val (label, color) = when (status) {
        StatementStatus.NOT_YET_BILLED -> "ยังไม่ตัดรอบ" to MaterialTheme.colorScheme.outline
        StatementStatus.BILLED -> "รอชำระ" to MaterialTheme.colorScheme.primary
        StatementStatus.PARTIALLY_PAID -> "ชำระบางส่วน" to MaterialTheme.colorScheme.tertiary
        StatementStatus.PAID -> "ชำระแล้ว" to MaterialTheme.colorScheme.primary
        StatementStatus.OVERDUE -> "เกินกำหนด" to BudgetOverColor
    }
    Surface(color = color.copy(alpha = 0.15f)) {
        Text(label, color = color, style = MaterialTheme.typography.labelSmall, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
    }
}
