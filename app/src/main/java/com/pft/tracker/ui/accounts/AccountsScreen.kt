package com.pft.tracker.ui.accounts

import androidx.compose.foundation.clickable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.size
import com.pft.tracker.ui.theme.BudgetOverColor
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.pft.tracker.di.AppContainer
import com.pft.tracker.di.ViewModelFactory
import com.pft.tracker.ui.common.formatBaht
import com.pft.tracker.ui.nav.Routes

@Composable
fun AccountsScreen(container: AppContainer, navController: NavController) {
    val viewModel: AccountsViewModel = viewModel(factory = ViewModelFactory { AccountsViewModel(container) })
    val state by viewModel.uiState.collectAsState()
    var accountToDelete by remember { mutableStateOf<com.pft.tracker.data.local.entity.AccountEntity?>(null) }

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("บัญชีและเงินสด", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        },
        bottomBar = {
            com.pft.tracker.ui.common.SummaryFooter(income = state.totalIncome, expense = state.totalExpense)
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(Routes.accountEdit()) },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(Icons.Rounded.Add, contentDescription = "เพิ่มบัญชี")
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.padding(padding),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp).let { 
                PaddingValues(it.calculateLeftPadding(androidx.compose.ui.unit.LayoutDirection.Ltr), it.calculateTopPadding(), it.calculateRightPadding(androidx.compose.ui.unit.LayoutDirection.Ltr), 80.dp) 
            },
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(state.accounts, key = { it.account.id }) { item ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { navController.navigate(Routes.accountDetail(item.account.id)) },
                    shape = MaterialTheme.shapes.medium
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(item.account.name, style = MaterialTheme.typography.titleMedium)
                            Text(
                                if (item.account.accountType == "CASH") "เงินสด" else (item.account.bankName ?: "ธนาคาร") + (item.account.accountNumberLast4?.let { " ••$it" } ?: ""),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                formatBaht(item.currentBalance),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            IconButton(onClick = { accountToDelete = item.account }) {
                                Icon(
                                    Icons.Rounded.Delete, 
                                    contentDescription = "ลบ", 
                                    tint = BudgetOverColor.copy(alpha = 0.7f), 
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    if (accountToDelete != null) {
        androidx.compose.material3.AlertDialog(
            onDismissRequest = { accountToDelete = null },
            title = { Text("ยืนยันการลบ") },
            text = { Text("คุณต้องการลบบัญชี '${accountToDelete?.name}' ใช่หรือไม่?") },
            confirmButton = {
                androidx.compose.material3.TextButton(
                    onClick = {
                        accountToDelete?.let { viewModel.delete(it) }
                        accountToDelete = null
                    },
                    colors = androidx.compose.material3.ButtonDefaults.textButtonColors(contentColor = BudgetOverColor)
                ) { Text("ลบ") }
            },
            dismissButton = {
                androidx.compose.material3.TextButton(onClick = { accountToDelete = null }) { Text("ยกเลิก") }
            }
        )
    }
}
