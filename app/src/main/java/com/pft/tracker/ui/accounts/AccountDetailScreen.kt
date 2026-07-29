package com.pft.tracker.ui.accounts

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
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.pft.tracker.di.AppContainer
import com.pft.tracker.di.ViewModelFactory
import com.pft.tracker.domain.BillingCycleUseCase
import com.pft.tracker.ui.common.formatBaht
import com.pft.tracker.ui.common.formatDate
import com.pft.tracker.ui.nav.Routes

@Composable
fun AccountDetailScreen(container: AppContainer, navController: NavController, accountId: Long) {
    val viewModel: AccountDetailViewModel = viewModel(factory = ViewModelFactory { AccountDetailViewModel(container, accountId) })
    val state by viewModel.uiState.collectAsState()
    val billingCycleUseCase = remember { BillingCycleUseCase() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(state.account?.name ?: "") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "ย้อนกลับ")
                    }
                },
                actions = {
                    IconButton(onClick = { navController.navigate(Routes.accountEdit(accountId)) }) {
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
                        Text("ยอดคงเหลือปัจจุบัน", style = MaterialTheme.typography.labelMedium)
                        Text(
                            formatBaht(state.balance),
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
            item {
                Text("ประวัติธุรกรรม", style = MaterialTheme.typography.titleMedium)
            }
            items(state.transactions, key = { it.id }) { tx ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(tx.title, style = MaterialTheme.typography.bodyLarge)
                            Text(
                                formatDate(billingCycleUseCase.epochToLocalDate(tx.transactionDate)),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Text(formatBaht(tx.amount), style = MaterialTheme.typography.bodyLarge)
                    }
                }
            }
        }
    }
}

