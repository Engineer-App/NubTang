package com.pft.tracker.ui.categories

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.pft.tracker.data.local.entity.CategoryEntity
import com.pft.tracker.di.AppContainer
import com.pft.tracker.di.ViewModelFactory
import com.pft.tracker.domain.model.CategoryType
import com.pft.tracker.ui.common.formatBaht

@Composable
fun CategoriesScreen(container: AppContainer, navController: NavController) {
    val viewModel: CategoriesViewModel = viewModel(factory = ViewModelFactory { CategoriesViewModel(container) })
    val categories by viewModel.categories.collectAsState()
    var selectedTab by remember { mutableIntStateOf(0) }
    var editingCategory by remember { mutableStateOf<CategoryEntity?>(null) }
    var showAddDialog by remember { mutableStateOf(false) }

    val currentType = if (selectedTab == 0) CategoryType.EXPENSE else CategoryType.INCOME
    val filtered = categories.filter { it.categoryType == currentType.name }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("หมวดหมู่และงบประมาณ") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "ย้อนกลับ")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddDialog = true }) {
                Icon(Icons.Filled.Add, contentDescription = "เพิ่มหมวดหมู่")
            }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            TabRow(selectedTabIndex = selectedTab) {
                Tab(selected = selectedTab == 0, onClick = { selectedTab = 0 }, text = { Text("รายจ่าย") })
                Tab(selected = selectedTab == 1, onClick = { selectedTab = 1 }, text = { Text("รายรับ") })
            }
            LazyColumn(contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(filtered, key = { it.id }) { category ->
                    Card(modifier = Modifier.fillMaxWidth().padding(0.dp)) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp)
                                .clickable { editingCategory = category },
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(category.name, style = MaterialTheme.typography.bodyLarge)
                                if (category.categoryType == CategoryType.EXPENSE.name) {
                                    Text(
                                        if (category.monthlyBudget != null && category.monthlyBudget > 0)
                                            "งบ ${formatBaht(category.monthlyBudget)}/เดือน"
                                        else "ไม่จำกัดงบ",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        CategoryEditDialog(
            existing = null,
            defaultType = currentType.name,
            onDismiss = { showAddDialog = false },
            onSave = { name, type, budget ->
                viewModel.save(0, name, type, budget, null)
                showAddDialog = false
            },
            onDelete = null
        )
    }

    editingCategory?.let { category ->
        CategoryEditDialog(
            existing = category,
            defaultType = category.categoryType,
            onDismiss = { editingCategory = null },
            onSave = { name, type, budget ->
                viewModel.save(category.id, name, type, budget, category)
                editingCategory = null
            },
            onDelete = {
                viewModel.delete(category)
                editingCategory = null
            }
        )
    }
}

@Composable
private fun CategoryEditDialog(
    existing: CategoryEntity?,
    defaultType: String,
    onDismiss: () -> Unit,
    onSave: (String, String, Double?) -> Unit,
    onDelete: (() -> Unit)?
) {
    var name by remember { mutableStateOf(existing?.name ?: "") }
    var budgetText by remember { mutableStateOf(existing?.monthlyBudget?.toString() ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (existing == null) "เพิ่มหมวดหมู่" else "แก้ไขหมวดหมู่") },
        text = {
            Column {
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("ชื่อหมวดหมู่") })
                if (defaultType == CategoryType.EXPENSE.name) {
                    OutlinedTextField(
                        value = budgetText,
                        onValueChange = { budgetText = it.filter { c -> c.isDigit() || c == '.' } },
                        label = { Text("งบประมาณต่อเดือน (เว้นว่าง = ไม่จำกัด)") }
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = { onSave(name, defaultType, budgetText.toDoubleOrNull()) }) { Text("บันทึก") }
        },
        dismissButton = {
            if (onDelete != null) {
                TextButton(onClick = onDelete) {
                    Icon(Icons.Filled.Delete, contentDescription = null)
                    Text(" ลบ")
                }
            } else {
                TextButton(onClick = onDismiss) { Text("ยกเลิก") }
            }
        }
    )
}
