package com.pft.tracker.ui.creditcards

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
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
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
import com.pft.tracker.data.local.entity.CreditLimitGroupEntity
import com.pft.tracker.di.AppContainer
import com.pft.tracker.di.ViewModelFactory
import com.pft.tracker.ui.common.formatBaht

@Composable
fun CreditLimitGroupsScreen(container: AppContainer, navController: NavController) {
    val viewModel: CreditLimitGroupsViewModel = viewModel(factory = ViewModelFactory { CreditLimitGroupsViewModel(container) })
    val groups by viewModel.groups.collectAsState()
    val allCards by viewModel.allCards.collectAsState()
    var editingGroup by remember { mutableStateOf<GroupRow?>(null) }
    var showAddDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("กลุ่มวงเงินร่วม") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "ย้อนกลับ")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddDialog = true }) {
                Icon(Icons.Filled.Add, contentDescription = "เพิ่มกลุ่ม")
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(groups, key = { it.group.id }) { row ->
                Card(
                    modifier = Modifier.fillMaxWidth().clickable { editingGroup = row }
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text(row.group.name, style = MaterialTheme.typography.titleMedium)
                        Text(
                            "ใช้ไป ${formatBaht(row.totalUsed)} จากวงเงินร่วม ${formatBaht(row.group.sharedLimit)}",
                            style = MaterialTheme.typography.bodySmall
                        )
                        Text(
                            if (row.memberCards.isEmpty()) "ยังไม่มีบัตรในกลุ่มนี้" else row.memberCards.joinToString(", ") { it.name },
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        GroupEditDialog(
            initial = null,
            onDismiss = { showAddDialog = false },
            onSave = { name, limit -> viewModel.saveGroup(0, name, limit); showAddDialog = false }
        )
    }

    editingGroup?.let { row ->
        GroupDetailDialog(
            row = row,
            allCards = allCards,
            onDismiss = { editingGroup = null },
            onSave = { name, limit -> viewModel.saveGroup(row.group.id, name, limit) },
            onDelete = { viewModel.deleteGroup(row.group); editingGroup = null },
            onToggleCard = { card, inGroup -> viewModel.setCardMembership(card, if (inGroup) row.group.id else null) }
        )
    }
}

@Composable
private fun GroupEditDialog(
    initial: CreditLimitGroupEntity?,
    onDismiss: () -> Unit,
    onSave: (String, Double) -> Unit
) {
    var name by remember { mutableStateOf(initial?.name ?: "") }
    var limitText by remember { mutableStateOf(initial?.sharedLimit?.toString() ?: "0") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (initial == null) "เพิ่มกลุ่มวงเงินร่วม" else "แก้ไขกลุ่ม") },
        text = {
            Column {
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("ชื่อกลุ่ม") })
                OutlinedTextField(
                    value = limitText,
                    onValueChange = { limitText = it.filter { c -> c.isDigit() || c == '.' } },
                    label = { Text("วงเงินร่วม") }
                )
            }
        },
        confirmButton = {
            TextButton(onClick = { onSave(name, limitText.toDoubleOrNull() ?: 0.0) }) { Text("บันทึก") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("ยกเลิก") }
        }
    )
}

@Composable
private fun GroupDetailDialog(
    row: GroupRow,
    allCards: List<com.pft.tracker.data.local.entity.CreditCardEntity>,
    onDismiss: () -> Unit,
    onSave: (String, Double) -> Unit,
    onDelete: () -> Unit,
    onToggleCard: (com.pft.tracker.data.local.entity.CreditCardEntity, Boolean) -> Unit
) {
    var name by remember { mutableStateOf(row.group.name) }
    var limitText by remember { mutableStateOf(row.group.sharedLimit.toString()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("แก้ไขกลุ่ม") },
        text = {
            Column {
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("ชื่อกลุ่ม") })
                OutlinedTextField(
                    value = limitText,
                    onValueChange = { limitText = it.filter { c -> c.isDigit() || c == '.' } },
                    label = { Text("วงเงินร่วม") }
                )
                Text("บัตรในกลุ่มนี้", style = MaterialTheme.typography.labelMedium, modifier = Modifier.padding(top = 8.dp))
                allCards.forEach { card ->
                    val inGroup = card.creditLimitGroupId == row.group.id
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(checked = inGroup, onCheckedChange = { onToggleCard(card, it) })
                        Text(card.name)
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = { onSave(name, limitText.toDoubleOrNull() ?: 0.0); onDismiss() }) { Text("บันทึก") }
        },
        dismissButton = {
            TextButton(onClick = onDelete) {
                Icon(Icons.Filled.Delete, contentDescription = null)
                Text(" ลบกลุ่ม")
            }
        }
    )
}
