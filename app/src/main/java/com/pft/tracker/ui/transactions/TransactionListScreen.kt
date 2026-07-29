package com.pft.tracker.ui.transactions

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ChevronLeft
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.size
import com.pft.tracker.ui.common.SummaryFooter
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.pft.tracker.di.AppContainer
import com.pft.tracker.di.ViewModelFactory
import com.pft.tracker.domain.model.TransactionType
import com.pft.tracker.ui.common.DropdownOption
import com.pft.tracker.ui.common.SimpleDropdownField
import com.pft.tracker.ui.nav.Routes
import com.pft.tracker.ui.theme.BudgetOverColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionListScreen(container: AppContainer, navController: NavController) {
    val viewModel: TransactionListViewModel = viewModel(factory = ViewModelFactory { TransactionListViewModel(container) })
    val state by viewModel.uiState.collectAsState()
    var showFilterDialog by remember { mutableStateOf(false) }
    var rowToDelete by remember { mutableStateOf<TransactionRow?>(null) }

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("รายการ", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        },
        bottomBar = {
            SummaryFooter(income = state.totalIncome, expense = state.totalExpense)
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { viewModel.setMonth(state.month.minusMonths(1)) }) {
                        Icon(Icons.Rounded.ChevronLeft, contentDescription = "เดือนก่อน")
                    }
                    Text(
                        com.pft.tracker.ui.common.monthLabel(state.month.year, state.month.monthValue),
                        style = MaterialTheme.typography.titleMedium
                    )
                    IconButton(onClick = { viewModel.setMonth(state.month.plusMonths(1)) }) {
                        Icon(Icons.Rounded.ChevronRight, contentDescription = "เดือนถัดไป")
                    }
                }
                TextButton(onClick = { showFilterDialog = true }) { Text("ตัวกรอง") }
            }

            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                FilterChip(
                    selected = state.filters.type == null,
                    onClick = { viewModel.setFilters(state.filters.copy(type = null)) },
                    label = { Text("ทั้งหมด") }
                )
                listOf(
                    TransactionType.EXPENSE to "รายจ่าย",
                    TransactionType.INCOME to "รายรับ",
                    TransactionType.TRANSFER to "การโอน"
                ).forEach { (type, label) ->
                    FilterChip(
                        selected = state.filters.type == type.name,
                        onClick = { viewModel.setFilters(state.filters.copy(type = type.name)) },
                        label = { Text(label) }
                    )
                }
            }

            if (state.rows.isEmpty()) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("ยังไม่มีธุรกรรมในเดือนนี้", style = MaterialTheme.typography.bodyMedium)
                }
            }

            LazyColumn(contentPadding = PaddingValues(bottom = 100.dp, top = 8.dp)) {
                items(state.rows, key = { it.entity.id }) { row ->
                    TransactionRowItem(
                        row = row,
                        onClick = {
                            navController.navigate(Routes.transactionEdit(row.entity.id, row.entity.transactionType))
                        },
                        onDelete = { rowToDelete = row }
                    )
                }
            }
        }
    }

    if (rowToDelete != null) {
        AlertDialog(
            onDismissRequest = { rowToDelete = null },
            title = { Text("ยืนยันการลบ") },
            text = { Text("คุณต้องการลบรายการ '${rowToDelete?.entity?.title}' ใช่หรือไม่?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        rowToDelete?.let { viewModel.delete(it) }
                        rowToDelete = null
                    },
                    colors = androidx.compose.material3.ButtonDefaults.textButtonColors(contentColor = BudgetOverColor)
                ) { Text("ลบ") }
            },
            dismissButton = {
                TextButton(onClick = { rowToDelete = null }) { Text("ยกเลิก") }
            }
        )
    }

    if (showFilterDialog) {
        FilterDialog(
            container = container,
            current = state.filters,
            onDismiss = { showFilterDialog = false },
            onApply = {
                viewModel.setFilters(it)
                showFilterDialog = false
            }
        )
    }
}

@Composable
private fun TransactionRowItem(row: TransactionRow, onClick: () -> Unit, onDelete: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 4.dp)
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(row.entity.title, style = MaterialTheme.typography.bodyLarge)
                    if (row.entity.isRecurringGenerated) {
                        Surface(
                            color = MaterialTheme.colorScheme.secondaryContainer,
                            modifier = Modifier.padding(start = 6.dp)
                        ) {
                            Text(
                                "ผ่อนอัตโนมัติ",
                                style = MaterialTheme.typography.labelSmall,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
                Text(
                    listOfNotNull(row.categoryLabel, row.sourceLabel, row.dateLabel).joinToString(" • "),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    (if (row.isPositive) "+" else "-") + row.amountDisplay,
                    color = if (row.isPositive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.bodyLarge
                )
                IconButton(onClick = onDelete) {
                    Icon(Icons.Rounded.Delete, contentDescription = "ลบ", tint = BudgetOverColor.copy(alpha = 0.7f), modifier = Modifier.size(20.dp))
                }
            }
        }
    }
}

@Composable
private fun FilterDialog(
    container: AppContainer,
    current: TransactionFilters,
    onDismiss: () -> Unit,
    onApply: (TransactionFilters) -> Unit
) {
    val categories by container.categoryRepository.observeAll().collectAsState(initial = emptyList())
    val accounts by container.accountRepository.observeAll().collectAsState(initial = emptyList())
    val cards by container.creditCardRepository.observeAll().collectAsState(initial = emptyList())

    var categoryId by remember { mutableStateOf(current.categoryId) }
    var accountId by remember { mutableStateOf(current.accountId) }
    var cardId by remember { mutableStateOf(current.cardId) }

    val accountCardOptions = remember(accounts, cards) {
        accounts.map { DropdownOption("acc_${it.id}", it.name) } +
            cards.map { DropdownOption("card_${it.id}", "${it.name} (บัตร)") }
    }
    val selectedAccountCardKey = when {
        accountId != null -> "acc_$accountId"
        cardId != null -> "card_$cardId"
        else -> null
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("ตัวกรองเพิ่มเติม") },
        text = {
            Column {
                SimpleDropdownField(
                    label = "หมวดหมู่",
                    options = categories.map { DropdownOption(it.id, it.name) },
                    selected = categoryId,
                    onSelect = { categoryId = it },
                    placeholder = "ทั้งหมด",
                    allowClear = true
                )
                Spacer(Modifier.padding(4.dp))
                SimpleDropdownField(
                    label = "บัญชี/บัตร",
                    options = accountCardOptions,
                    selected = selectedAccountCardKey,
                    onSelect = { key ->
                        if (key == null) {
                            accountId = null; cardId = null
                        } else if (key.startsWith("acc_")) {
                            accountId = key.removePrefix("acc_").toLong(); cardId = null
                        } else {
                            cardId = key.removePrefix("card_").toLong(); accountId = null
                        }
                    },
                    placeholder = "ทั้งหมด",
                    allowClear = true
                )
            }
        },
        confirmButton = {
            TextButton(onClick = {
                onApply(current.copy(categoryId = categoryId, accountId = accountId, cardId = cardId))
            }) { Text("ใช้ตัวกรอง") }
        },
        dismissButton = {
            TextButton(onClick = {
                onApply(current.copy(categoryId = null, accountId = null, cardId = null))
            }) { Text("ล้างตัวกรอง") }
        }
    )
}
