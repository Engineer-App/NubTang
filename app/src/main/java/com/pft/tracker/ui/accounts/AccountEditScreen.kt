package com.pft.tracker.ui.accounts

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
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.pft.tracker.di.AppContainer
import com.pft.tracker.di.ViewModelFactory

@Composable
fun AccountEditScreen(container: AppContainer, navController: NavController, accountId: Long) {
    val viewModel: AccountEditViewModel = viewModel(factory = ViewModelFactory { AccountEditViewModel(container, accountId) })
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(state.saved) {
        if (state.saved) navController.popBackStack()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (accountId == 0L) "เพิ่มบัญชี" else "แก้ไขบัญชี") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "ย้อนกลับ")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding).padding(16.dp).verticalScroll(rememberScrollState())
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(selected = state.accountType == "CASH", onClick = { viewModel.setAccountType("CASH") })
                Text("เงินสด")
                Spacer(Modifier.padding(horizontal = 8.dp))
                RadioButton(selected = state.accountType == "BANK", onClick = { viewModel.setAccountType("BANK") })
                Text("บัญชีธนาคาร")
            }
            Spacer(Modifier.padding(6.dp))

            Text("ความเป็นเจ้าของ", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(selected = state.isOwnedBySelf, onClick = { viewModel.setIsOwnedBySelf(true) })
                Text("บัญชีตัวเอง")
                Spacer(Modifier.padding(horizontal = 8.dp))
                RadioButton(selected = !state.isOwnedBySelf, onClick = { viewModel.setIsOwnedBySelf(false) })
                Text("บัญชีผู้อื่น")
            }
            Spacer(Modifier.padding(6.dp))
            OutlinedTextField(
                value = state.name,
                onValueChange = viewModel::setName,
                label = { Text("ชื่อบัญชี") },
                modifier = Modifier.fillMaxWidth()
            )
            if (state.accountType == "BANK") {
                Spacer(Modifier.padding(6.dp))
                OutlinedTextField(
                    value = state.bankName,
                    onValueChange = viewModel::setBankName,
                    label = { Text("ชื่อธนาคาร") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.padding(6.dp))
                OutlinedTextField(
                    value = state.last4,
                    onValueChange = viewModel::setLast4,
                    label = { Text("เลขบัญชี 4 หลักสุดท้าย") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Spacer(Modifier.padding(6.dp))
            OutlinedTextField(
                value = state.openingBalanceText,
                onValueChange = viewModel::setOpeningBalanceText,
                label = { Text("ยอดยกมาเริ่มต้น") },
                modifier = Modifier.fillMaxWidth()
            )
            if (accountId != 0L) {
                Spacer(Modifier.padding(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("เปิดใช้งานบัญชีนี้")
                    Switch(checked = state.isActive, onCheckedChange = viewModel::setActive)
                }
            }
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
}
