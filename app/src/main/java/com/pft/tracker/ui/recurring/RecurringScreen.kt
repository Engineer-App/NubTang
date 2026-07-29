package com.pft.tracker.ui.recurring

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
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
import com.pft.tracker.data.local.entity.RecurringTransactionEntity
import com.pft.tracker.di.AppContainer
import com.pft.tracker.di.ViewModelFactory
import com.pft.tracker.ui.common.formatBaht
import com.pft.tracker.ui.nav.Routes

@Composable
fun RecurringScreen(container: AppContainer, navController: NavController) {
    val viewModel: RecurringViewModel = viewModel(factory = ViewModelFactory { RecurringViewModel(container) })
    val plans by viewModel.plans.collectAsState()
    var deleteTarget by remember { mutableStateOf<RecurringTransactionEntity?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("รายการล่วงหน้า/ผ่อนชำระ") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "ย้อนกลับ")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate(Routes.recurringEdit()) }) {
                Icon(Icons.Filled.Add, contentDescription = "เพิ่มแผน")
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(plans, key = { it.id }) { plan ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { navController.navigate(Routes.recurringEdit(plan.id)) }
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(plan.name, style = MaterialTheme.typography.titleMedium)
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Switch(checked = plan.isActive, onCheckedChange = { viewModel.toggleActive(plan) })
                                IconButton(onClick = { deleteTarget = plan }) {
                                    Icon(Icons.Filled.Delete, contentDescription = "ลบ")
                                }
                            }
                        }
                        Text("${formatBaht(plan.amount)} / งวด", style = MaterialTheme.typography.bodyMedium)
                        if (plan.totalInstallments != null) {
                            androidx.compose.foundation.layout.Spacer(Modifier.padding(2.dp))
                            LinearProgressIndicator(
                                progress = { plan.installmentsGenerated.toFloat() / plan.totalInstallments.toFloat() },
                                modifier = Modifier.fillMaxWidth()
                            )
                            Text(
                                "งวดที่ ${plan.installmentsGenerated}/${plan.totalInstallments}",
                                style = MaterialTheme.typography.bodySmall
                            )
                        } else {
                            Text("รายการประจำไม่มีกำหนดสิ้นสุด", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            }
        }
    }

    deleteTarget?.let { plan ->
        AlertDialog(
            onDismissRequest = { deleteTarget = null },
            title = { Text("ลบแผน '${plan.name}'?") },
            text = { Text("รายการที่สร้างไปแล้วจะไม่ถูกลบ") },
            confirmButton = {
                TextButton(onClick = { viewModel.delete(plan); deleteTarget = null }) { Text("ลบ") }
            },
            dismissButton = {
                TextButton(onClick = { deleteTarget = null }) { Text("ยกเลิก") }
            }
        )
    }
}
