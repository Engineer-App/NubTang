package com.pft.tracker.ui.dashboard

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.pft.tracker.di.AppContainer
import com.pft.tracker.di.ViewModelFactory
import com.pft.tracker.ui.common.SimplePieChart
import com.pft.tracker.ui.common.StackedBudgetBarChart
import com.pft.tracker.ui.common.formatBaht
import com.pft.tracker.ui.theme.BudgetOverColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(container: AppContainer, navController: NavController) {
    val viewModel: DashboardViewModel = viewModel(factory = ViewModelFactory { DashboardViewModel(container) })
    val state by viewModel.uiState.collectAsState()
    var showNameEdit by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            CenterAlignedTopAppBar(
                title = { 
                    Row(
                        modifier = Modifier.clickable { showNameEdit = true },
                        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                    ) {
                        Text("บัญชี ${state.userName}", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                        androidx.compose.foundation.layout.Spacer(Modifier.padding(4.dp))
                        Icon(androidx.compose.material.icons.Icons.Rounded.Edit, contentDescription = "แก้ไขชื่อ", modifier = Modifier.size(16.dp))
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        }
    ) { padding ->
        LazyColumn(
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            modifier = Modifier.padding(padding)
        ) {
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf(
                        DashboardPeriod.THIS_MONTH to "เดือนนี้",
                        DashboardPeriod.LAST_MONTH to "เดือนก่อน",
                        DashboardPeriod.THIS_YEAR to "ปีนี้"
                    ).forEach { (period, label) ->
                        FilterChip(
                            selected = state.period == period,
                            onClick = { viewModel.selectPeriod(period) },
                            label = { Text(label) }
                        )
                    }
                }
            }

            item {
                SummaryCards(state)
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth().clickable { viewModel.toggleBudgetDetails() },
                    shape = MaterialTheme.shapes.medium
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text("ค่าใช้จ่ายเทียบงบประมาณ", style = MaterialTheme.typography.titleMedium)
                        androidx.compose.foundation.layout.Spacer(Modifier.padding(4.dp))
                        StackedBudgetBarChart(state.budgetStatuses)
                        if (state.budgetStatuses.any { it.overBudget }) {
                            Text(
                                "สีแดง = เกินงบประมาณ",
                                color = BudgetOverColor,
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                        if (state.showBudgetDetails) {
                            BudgetDetailsTable(state.budgetStatuses)
                        }
                    }
                }
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth().clickable { viewModel.toggleSourceDetails() },
                    shape = MaterialTheme.shapes.medium
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text("ค่าใช้จ่ายแยกตามบัญชี/บัตร", style = MaterialTheme.typography.titleMedium)
                        androidx.compose.foundation.layout.Spacer(Modifier.padding(4.dp))
                        SimplePieChart(state.expensePieSlices)
                        if (state.showSourceDetails) {
                            SourceDetailsTable(state.expensePieSlices)
                        }
                    }
                }
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text("เทียบกับเดือนก่อน", style = MaterialTheme.typography.titleMedium)
                        androidx.compose.foundation.layout.Spacer(Modifier.padding(4.dp))
                        ComparisonRow("รายจ่าย", state.totalExpense, state.previousExpense)
                        ComparisonRow("รายรับ", state.totalIncome, state.previousIncome)
                    }
                }
            }
        }
    }

    if (showNameEdit) {
        var newName by remember { mutableStateOf(state.userName) }
        AlertDialog(
            onDismissRequest = { showNameEdit = false },
            title = { Text("แก้ไขชื่อผู้ใช้งาน") },
            text = {
                OutlinedTextField(
                    value = newName,
                    onValueChange = { newName = it },
                    label = { Text("ชื่อ") },
                    singleLine = true
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.setUserName(newName)
                    showNameEdit = false
                }) { Text("ตกลง") }
            },
            dismissButton = {
                TextButton(onClick = { showNameEdit = false }) { Text("ยกเลิก") }
            }
        )
    }
}

@Composable
private fun SummaryCards(state: DashboardUiState) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            SummaryCard("รายจ่ายรวม", formatBaht(state.totalExpense), Modifier.weight(1f), MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.3f))
            SummaryCard("รายรับรวม", formatBaht(state.totalIncome), Modifier.weight(1f), MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f))
        }
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            SummaryCard("เงินสดคงเหลือ", formatBaht(state.cashTotal), Modifier.weight(1f))
            SummaryCard("เงินในบัญชีรวม", formatBaht(state.bankTotal), Modifier.weight(1f))
        }
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            SummaryCard("ยอดใช้บัตรค้างชำระ", formatBaht(state.creditCardDebt), Modifier.weight(1f))
            SummaryCard("ยอดคงเหลือสุทธิ", formatBaht(state.netBalance), Modifier.weight(1f), MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.4f))
        }
    }
}

@Composable
private fun SummaryCard(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    containerColor: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
) {
    Card(
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = containerColor)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(label, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(value, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
        }
    }
}

@Composable
private fun ComparisonRow(label: String, current: Double, previous: Double) {
    val diff = current - previous
    val diffPct = if (previous != 0.0) (diff / previous) * 100 else 0.0
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, style = MaterialTheme.typography.bodyMedium)
        Text(
            "${formatBaht(current)}  (${if (diff >= 0) "+" else ""}${"%.1f".format(diffPct)}%)",
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

@Composable
private fun BudgetDetailsTable(statuses: List<com.pft.tracker.domain.CategoryBudgetStatus>) {
    val nonZero = statuses.filter { it.spent > 0 || (it.budget != null && it.budget > 0) }
    Column(modifier = Modifier.padding(top = 16.dp)) {
        Row(modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)) {
            Text("ลำดับ", modifier = Modifier.weight(0.5f), fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodySmall)
            Text("หมวดหมู่", modifier = Modifier.weight(1.5f), fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodySmall)
            Text("ใช้ไป", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodySmall)
            Text("งบประมาณ", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodySmall)
        }
        nonZero.forEachIndexed { index, status ->
            Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                Text((index + 1).toString(), modifier = Modifier.weight(0.5f), style = MaterialTheme.typography.bodySmall)
                Text(status.categoryName, modifier = Modifier.weight(1.5f), style = MaterialTheme.typography.bodySmall)
                Text(formatBaht(status.spent), modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodySmall)
                Text(status.budget?.let { formatBaht(it) } ?: "ไม่จำกัด", modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

@Composable
private fun SourceDetailsTable(slices: List<com.pft.tracker.ui.common.PieSlice>) {
    val nonZero = slices.filter { it.value > 0 }
    Column(modifier = Modifier.padding(top = 16.dp)) {
        Row(modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)) {
            Text("บัญชี/บัตร", modifier = Modifier.weight(2f), fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodySmall)
            Text("จำนวนเงิน", modifier = Modifier.weight(1.5f), fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodySmall)
        }
        nonZero.forEach { slice ->
            Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                Text(slice.label, modifier = Modifier.weight(2f), style = MaterialTheme.typography.bodySmall)
                Text(formatBaht(slice.value), modifier = Modifier.weight(1.5f), style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}
