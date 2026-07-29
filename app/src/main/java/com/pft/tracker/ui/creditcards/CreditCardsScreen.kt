package com.pft.tracker.ui.creditcards

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.pft.tracker.di.AppContainer
import com.pft.tracker.di.ViewModelFactory
import com.pft.tracker.ui.common.formatBaht
import com.pft.tracker.ui.nav.Routes
import com.pft.tracker.ui.theme.BudgetOverColor

@Composable
fun CreditCardsScreen(container: AppContainer, navController: NavController) {
    val viewModel: CreditCardsViewModel = viewModel(factory = ViewModelFactory { CreditCardsViewModel(container) })
    val state by viewModel.uiState.collectAsState()
    var cardToDelete by remember { mutableStateOf<com.pft.tracker.data.local.entity.CreditCardEntity?>(null) }

    Scaffold(
        containerColor = androidx.compose.ui.graphics.Color.Transparent,
        topBar = {
            androidx.compose.material3.CenterAlignedTopAppBar(
                title = { Text("บัตรเครดิต", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold) },
                colors = androidx.compose.material3.TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = androidx.compose.ui.graphics.Color.Transparent
                ),
                actions = {
                    IconButton(onClick = { navController.navigate(Routes.CREDIT_LIMIT_GROUPS) }) {
                        Icon(Icons.Filled.Group, contentDescription = "กลุ่มวงเงินร่วม")
                    }
                }
            )
        },
        bottomBar = {
            com.pft.tracker.ui.common.SummaryFooter(income = state.totalIncome, expense = state.totalExpense)
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate(Routes.creditCardEdit()) }) {
                Icon(Icons.Filled.Add, contentDescription = "เพิ่มบัตร")
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.padding(padding),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp).let { 
                PaddingValues(it.calculateLeftPadding(androidx.compose.ui.unit.LayoutDirection.Ltr), it.calculateTopPadding(), it.calculateRightPadding(androidx.compose.ui.unit.LayoutDirection.Ltr), 80.dp) 
            },
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(state.cards, key = { it.card.id }) { row ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { navController.navigate(Routes.creditCardDetail(row.card.id)) }
                ) {
                    Column(Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(row.card.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                                Text("••${row.card.cardNumberLast4}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                            IconButton(onClick = { cardToDelete = row.card }) {
                                Icon(
                                    androidx.compose.material.icons.Icons.Rounded.Delete, 
                                    contentDescription = "ลบ", 
                                    tint = BudgetOverColor.copy(alpha = 0.7f), 
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                        
                        if (row.groupMateNames.isNotEmpty()) {
                            Surface(
                                color = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.5f),
                                shape = MaterialTheme.shapes.extraSmall,
                                modifier = Modifier.padding(top = 4.dp)
                            ) {
                                Text(
                                    "วงเงินร่วมกับ ${row.groupMateNames.joinToString(", ")}",
                                    style = MaterialTheme.typography.labelSmall,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                        
                        androidx.compose.foundation.layout.Spacer(Modifier.height(8.dp))
                        val progress = if (row.effectiveLimit > 0) (row.currentUsed / row.effectiveLimit).toFloat().coerceIn(0f, 1f) else 0f
                        LinearProgressIndicator(
                            progress = { progress },
                            modifier = Modifier.fillMaxWidth(),
                            color = if (row.availableLimit < 0) BudgetOverColor else MaterialTheme.colorScheme.primary,
                            trackColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                        
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("ใช้ไป ${formatBaht(row.currentUsed)}", style = MaterialTheme.typography.labelSmall)
                            Text("คงเหลือ ${formatBaht(row.availableLimit)}", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                        }
                        
                        Text(
                            "ตัดรอบวันที่ ${row.card.statementDay} • ครบกำหนดวันที่ ${row.card.paymentDueDay}",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }

    if (cardToDelete != null) {
        androidx.compose.material3.AlertDialog(
            onDismissRequest = { cardToDelete = null },
            title = { Text("ยืนยันการลบ") },
            text = { Text("คุณต้องการลบสบัตร '${cardToDelete?.name}' ใช่หรือไม่?") },
            confirmButton = {
                androidx.compose.material3.TextButton(
                    onClick = {
                        cardToDelete?.let { viewModel.delete(it) }
                        cardToDelete = null
                    },
                    colors = androidx.compose.material3.ButtonDefaults.textButtonColors(contentColor = BudgetOverColor)
                ) { Text("ลบ") }
            },
            dismissButton = {
                androidx.compose.material3.TextButton(onClick = { cardToDelete = null }) { Text("ยกเลิก") }
            }
        )
    }
}
