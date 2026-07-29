package com.pft.tracker.ui.creditcards

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.pft.tracker.di.AppContainer
import com.pft.tracker.di.ViewModelFactory
import com.pft.tracker.ui.common.DropdownOption
import com.pft.tracker.ui.common.SimpleDropdownField

@Composable
fun CreditCardEditScreen(container: AppContainer, navController: NavController, cardId: Long) {
    val viewModel: CreditCardEditViewModel = viewModel(factory = ViewModelFactory { CreditCardEditViewModel(container, cardId) })
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(state.saved) {
        if (state.saved) navController.popBackStack()
    }

    Scaffold(
        modifier = Modifier.imePadding(),
        topBar = {
            TopAppBar(
                title = { Text(if (cardId == 0L) "เพิ่มบัตรเครดิต" else "แก้ไขบัตรเครดิต") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "ย้อนกลับ")
                    }
                }
            )
        }
    ) { padding ->
        androidx.compose.foundation.layout.Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            OutlinedTextField(
                value = state.name,
                onValueChange = viewModel::setName,
                label = { Text("ชื่อบัตร") },
                modifier = Modifier.fillMaxWidth()
            )
            androidx.compose.foundation.layout.Spacer(Modifier.padding(6.dp))
            OutlinedTextField(
                value = state.last4,
                onValueChange = viewModel::setLast4,
                label = { Text("เลขบัตร 4 หลักสุดท้าย") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            androidx.compose.foundation.layout.Spacer(Modifier.padding(6.dp))

            SimpleDropdownField(
                label = "กลุ่มวงเงินร่วม",
                options = state.groups.map { DropdownOption(it.id, it.name) },
                selected = state.groupId,
                onSelect = viewModel::setGroupId,
                placeholder = "ไม่ใช้กลุ่ม (วงเงินของบัตรนี้เอง)",
                allowClear = true,
                modifier = Modifier.fillMaxWidth()
            )
            androidx.compose.foundation.layout.Spacer(Modifier.padding(6.dp))

            if (state.groupId == null) {
                OutlinedTextField(
                    value = state.creditLimitText,
                    onValueChange = viewModel::setCreditLimitText,
                    label = { Text("วงเงิน") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                )
                androidx.compose.foundation.layout.Spacer(Modifier.padding(6.dp))
            }

            SimpleDropdownField(
                label = "ตัดรอบทุกกี่เดือน",
                options = (1..3).map { DropdownOption(it, if (it == 1) "ทุกเดือน" else "ทุก $it เดือน") },
                selected = state.billingFrequencyMonths,
                onSelect = { viewModel.setBillingFrequencyMonths(it ?: 1) },
                modifier = Modifier.fillMaxWidth()
            )
            androidx.compose.foundation.layout.Spacer(Modifier.padding(6.dp))

            OutlinedTextField(
                value = state.statementDayText,
                onValueChange = viewModel::setStatementDayText,
                label = { Text("วันตัดรอบ (1-31)") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            androidx.compose.foundation.layout.Spacer(Modifier.padding(6.dp))
            OutlinedTextField(
                value = state.paymentDueDayText,
                onValueChange = viewModel::setPaymentDueDayText,
                label = { Text("วันครบกำหนดชำระ (1-31)") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            if (state.billingFrequencyMonths > 1) {
                androidx.compose.foundation.layout.Spacer(Modifier.padding(6.dp))
                SimpleDropdownField(
                    label = "เดือนเริ่มต้นรอบ (anchor)",
                    options = (1..12).map { DropdownOption(it, "เดือน $it") },
                    selected = state.startMonth,
                    onSelect = viewModel::setStartMonth,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            if (state.error != null) {
                androidx.compose.foundation.layout.Spacer(Modifier.padding(6.dp))
                Text(state.error!!, color = MaterialTheme.colorScheme.error)
            }

            androidx.compose.foundation.layout.Spacer(Modifier.padding(16.dp))
            Button(onClick = viewModel::save, modifier = Modifier.fillMaxWidth()) {
                Text("บันทึก")
            }
        }
    }
}
