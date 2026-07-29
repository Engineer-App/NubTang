package com.pft.tracker.ui.recurring

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.pft.tracker.domain.model.RecurringFrequency
import com.pft.tracker.ui.common.DropdownOption
import com.pft.tracker.ui.common.SimpleDropdownField
import com.pft.tracker.ui.common.formatDate
import java.time.Instant
import java.time.ZoneId

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecurringEditScreen(container: AppContainer, navController: NavController, planId: Long) {
    val viewModel: RecurringEditViewModel = viewModel(factory = ViewModelFactory { RecurringEditViewModel(container, planId) })
    val state by viewModel.uiState.collectAsState()
    var showDatePicker by remember { mutableStateOf(false) }

    LaunchedEffect(state.saved) {
        if (state.saved) navController.popBackStack()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (planId == 0L) "เพิ่มแผนผ่อนชำระ" else "แก้ไขแผน") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "ย้อนกลับ")
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp).verticalScroll(rememberScrollState())) {
            OutlinedTextField(value = state.name, onValueChange = viewModel::setName, label = { Text("ชื่อแผน") }, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.padding(6.dp))
            OutlinedTextField(value = state.amountText, onValueChange = viewModel::setAmountText, label = { Text("จำนวนเงินต่องวด") }, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.padding(6.dp))

            SimpleDropdownField(
                label = "หมวดหมู่",
                options = state.categories.map { DropdownOption(it.id, it.name) },
                selected = state.categoryId,
                onSelect = viewModel::setCategory,
                allowClear = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.padding(6.dp))

            val combined = state.accounts.map { DropdownOption("acc_${it.id}", it.name) } +
                state.cards.map { DropdownOption("card_${it.id}", "${it.name} (บัตร)") }
            val selectedKey = state.sourceAccountId?.let { "acc_$it" } ?: state.sourceCreditCardId?.let { "card_$it" }
            SimpleDropdownField(
                label = "จ่ายจาก (บัญชี/บัตร)",
                options = combined,
                selected = selectedKey,
                onSelect = { key ->
                    when {
                        key == null -> viewModel.setSource(null, null)
                        key.startsWith("acc_") -> viewModel.setSource(key.removePrefix("acc_").toLong(), null)
                        else -> viewModel.setSource(null, key.removePrefix("card_").toLong())
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.padding(6.dp))

            OutlinedButton(onClick = { showDatePicker = true }, modifier = Modifier.fillMaxWidth()) {
                Text("วันที่เริ่ม: ${formatDate(state.startDate)}")
            }
            Spacer(Modifier.padding(6.dp))

            SimpleDropdownField(
                label = "ความถี่",
                options = listOf(
                    DropdownOption(RecurringFrequency.MONTHLY, "รายเดือน"),
                    DropdownOption(RecurringFrequency.LAST_DAY_OF_MONTH, "ทุกสิ้นเดือน"),
                    DropdownOption(RecurringFrequency.WEEKLY, "รายสัปดาห์"),
                    DropdownOption(RecurringFrequency.YEARLY, "รายปี"),
                    DropdownOption(RecurringFrequency.ONE_TIME, "ครั้งเดียว")
                ),
                selected = state.frequency,
                onSelect = { it?.let(viewModel::setFrequency) },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.padding(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("กำหนดจำนวนงวด")
                Switch(checked = state.hasFixedInstallments, onCheckedChange = viewModel::setHasFixedInstallments)
            }
            if (state.hasFixedInstallments) {
                OutlinedTextField(
                    value = state.totalInstallmentsText,
                    onValueChange = viewModel::setTotalInstallmentsText,
                    label = { Text("จำนวนงวดทั้งหมด") },
                    modifier = Modifier.fillMaxWidth()
                )
            } else {
                Text("รายการประจำถาวร (เช่น ค่าเช่า) — ไม่มีวันสิ้นสุด", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Spacer(Modifier.padding(6.dp))

            OutlinedTextField(value = state.note, onValueChange = viewModel::setNote, label = { Text("หมายเหตุ") }, modifier = Modifier.fillMaxWidth())

            if (state.error != null) {
                Spacer(Modifier.padding(6.dp))
                Text(state.error!!, color = MaterialTheme.colorScheme.error)
            }

            Spacer(Modifier.padding(16.dp))
            Button(onClick = viewModel::save, modifier = Modifier.fillMaxWidth()) {
                Text("บันทึก")
            }
        }
    }

    if (showDatePicker) {
        val datePickerState = androidx.compose.material3.rememberDatePickerState(
            initialSelectedDateMillis = state.startDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
        )
        androidx.compose.material3.DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                androidx.compose.material3.TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        viewModel.setStartDate(Instant.ofEpochMilli(millis).atZone(ZoneId.of("UTC")).toLocalDate())
                    }
                    showDatePicker = false
                }) { Text("ตกลง") }
            },
            dismissButton = {
                androidx.compose.material3.TextButton(onClick = { showDatePicker = false }) { Text("ยกเลิก") }
            }
        ) {
            androidx.compose.material3.DatePicker(state = datePickerState)
        }
    }
}
