package com.pft.tracker.ui.transactions

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.imePadding
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.pft.tracker.di.AppContainer
import com.pft.tracker.di.ViewModelFactory
import com.pft.tracker.domain.model.CategoryType
import com.pft.tracker.domain.model.TransactionType
import com.pft.tracker.ui.common.DropdownOption
import com.pft.tracker.ui.common.SimpleDropdownField
import com.pft.tracker.ui.common.formatDate
import java.time.Instant
import java.time.ZoneId

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditTransactionScreen(
    container: AppContainer,
    navController: NavController,
    transactionId: Long,
    initialType: String
) {
    val viewModel: AddEditTransactionViewModel = viewModel(
        factory = ViewModelFactory { AddEditTransactionViewModel(container, transactionId, initialType) }
    )
    val state by viewModel.uiState.collectAsState()
    var showDeleteConfirm by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }
    val snackbarHostState = remember { androidx.compose.material3.SnackbarHostState() }
    val scope = androidx.compose.runtime.rememberCoroutineScope()

    LaunchedEffect(state.saved, state.deleted) {
        if (state.saved) {
            if (state.savedEffectiveMonth != null) {
                snackbarHostState.showSnackbar("บันทึกแล้ว (รายการนี้ถูกนับเป็นยอดของเดือน ${state.savedEffectiveMonth})")
                kotlinx.coroutines.delay(2000)
            }
            navController.popBackStack()
        } else if (state.deleted) {
            navController.popBackStack()
        }
    }

    val context = androidx.compose.ui.platform.LocalContext.current
    val receiptLauncher = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        if (uri != null) {
            val copiedUri = com.pft.tracker.util.MediaHelper.copyToNabTangAlbum(context, uri)
            val finalUri = copiedUri ?: uri
            
            if (copiedUri == null) {
                // Fallback to original URI if copy failed
                runCatching {
                    context.contentResolver.takePersistableUriPermission(
                        finalUri,
                        android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION
                    )
                }
            }
            viewModel.setReceiptPath(finalUri.toString())
        }
    }

    Scaffold(
        modifier = Modifier.imePadding(),
        snackbarHost = { androidx.compose.material3.SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text(typeTitle(state.type, state.isEditing)) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "ย้อนกลับ")
                    }
                },
                actions = {
                    if (state.isEditing) {
                        if (state.isReadOnly) {
                            IconButton(onClick = { viewModel.setReadOnly(false) }) {
                                Icon(androidx.compose.material.icons.Icons.Rounded.Edit, contentDescription = "แก้ไข")
                            }
                        }
                        IconButton(onClick = { showDeleteConfirm = true }) {
                            Icon(Icons.Filled.Delete, contentDescription = "ลบ")
                        }
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            OutlinedButton(onClick = { showDatePicker = true }, modifier = Modifier.fillMaxWidth()) {
                Text("วันที่: ${formatDate(state.date)}")
            }
            Spacer(Modifier.padding(6.dp))

            OutlinedTextField(
                value = state.title,
                onValueChange = viewModel::setTitle,
                label = { Text("รายการ") },
                modifier = Modifier.fillMaxWidth(),
                enabled = !state.isReadOnly
            )
            Spacer(Modifier.padding(6.dp))

            if (state.type == TransactionType.EXPENSE || state.type == TransactionType.INCOME || state.type == TransactionType.TRANSFER) {
                val categoryType = if (state.type == TransactionType.INCOME) CategoryType.INCOME else CategoryType.EXPENSE
                SimpleDropdownField(
                    label = "หมวดหมู่",
                    options = state.categories.filter { it.categoryType == categoryType.name }.map { DropdownOption(it.id, it.name) },
                    selected = state.categoryId,
                    onSelect = viewModel::setCategory,
                    allowClear = true,
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !state.isReadOnly
                )
                Spacer(Modifier.padding(6.dp))
            }

            OutlinedTextField(
                value = state.amountText,
                onValueChange = viewModel::setAmountText,
                label = { Text("จำนวนเงิน") },
                modifier = Modifier.fillMaxWidth(),
                enabled = !state.isReadOnly
            )
            Spacer(Modifier.padding(10.dp))

            SourceDestinationFields(state, viewModel)

            Spacer(Modifier.padding(6.dp))
            OutlinedTextField(
                value = state.note,
                onValueChange = viewModel::setNote,
                label = { Text("หมายเหตุ") },
                modifier = Modifier.fillMaxWidth(),
                enabled = !state.isReadOnly
            )
            Spacer(Modifier.padding(10.dp))

            OutlinedButton(
                onClick = { receiptLauncher.launch(arrayOf("image/*")) }, 
                modifier = Modifier.fillMaxWidth(),
                enabled = !state.isReadOnly
            ) {
                Icon(Icons.Filled.AttachFile, contentDescription = null)
                Spacer(Modifier.padding(4.dp))
                Text(if (state.receiptPath != null) "แนบใบเสร็จแล้ว (แตะเพื่อเปลี่ยน)" else "แนบรูปใบเสร็จ")
            }

            if (state.error != null) {
                Spacer(Modifier.padding(6.dp))
                Text(state.error!!, color = MaterialTheme.colorScheme.error)
            }

            if (!state.isReadOnly) {
                Spacer(Modifier.padding(12.dp))
                Button(onClick = viewModel::save, modifier = Modifier.fillMaxWidth()) {
                    Text("บันทึก")
                }
            }
        }
    }

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = state.date.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
        )
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        viewModel.setDate(Instant.ofEpochMilli(millis).atZone(ZoneId.of("UTC")).toLocalDate())
                    }
                    showDatePicker = false
                }) { Text("ตกลง") }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("ยกเลิก") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    if (showDeleteConfirm) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirm = false },
            title = { Text("ลบรายการนี้?") },
            text = { Text("การลบไม่สามารถย้อนกลับได้") },
            confirmButton = {
                TextButton(onClick = { showDeleteConfirm = false; viewModel.delete() }) { Text("ลบ") }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirm = false }) { Text("ยกเลิก") }
            }
        )
    }
}

private fun typeTitle(type: TransactionType, isEditing: Boolean): String {
    val prefix = if (isEditing) "แก้ไข" else "เพิ่ม"
    val label = when (type) {
        TransactionType.EXPENSE -> "รายจ่าย"
        TransactionType.INCOME -> "รายรับ"
        TransactionType.TRANSFER -> "โอนเงิน"
        TransactionType.CASH_WITHDRAWAL -> "ถอนเงินสด"
        TransactionType.CREDIT_CARD_PAYMENT -> "ชำระบัตรเครดิต"
    }
    return "$prefix$label"
}

@Composable
private fun SourceDestinationFields(state: AddEditTransactionUiState, viewModel: AddEditTransactionViewModel) {
    val accountOptions = state.accounts.map { DropdownOption(it.id, if (it.isOwnedBySelf) it.name else "${it.name} (ผู้อื่น)") }
    val cardOptions = state.cards.map { DropdownOption(it.id, it.name) }
    val cashAccountOptions = state.accounts.filter { it.accountType == "CASH" && it.isOwnedBySelf }.map { DropdownOption(it.id, it.name) }
    val bankAccountOptions = state.accounts.filter { it.accountType == "BANK" && it.isOwnedBySelf }.map { DropdownOption(it.id, it.name) }

    when (state.type) {
        TransactionType.EXPENSE -> {
            val combined = accountOptions.map { DropdownOption("acc_${it.value}", it.label) } +
                cardOptions.map { DropdownOption("card_${it.value}", "${it.label} (บัตร)") }
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
        }
        TransactionType.INCOME -> {
            SimpleDropdownField(
                label = "เข้าบัญชี",
                options = accountOptions,
                selected = state.destinationAccountId,
                onSelect = { viewModel.setDestination(it, null) },
                modifier = Modifier.fillMaxWidth()
            )
        }
        TransactionType.TRANSFER -> {
            SimpleDropdownField(
                label = "จากบัญชี",
                options = accountOptions,
                selected = state.sourceAccountId,
                onSelect = { viewModel.setSource(it, null) },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.padding(6.dp))
            SimpleDropdownField(
                label = "ไปบัญชี",
                options = accountOptions,
                selected = state.destinationAccountId,
                onSelect = { viewModel.setDestination(it, null) },
                modifier = Modifier.fillMaxWidth()
            )
        }
        TransactionType.CASH_WITHDRAWAL -> {
            SimpleDropdownField(
                label = "จากบัญชีธนาคาร",
                options = bankAccountOptions.ifEmpty { accountOptions },
                selected = state.sourceAccountId,
                onSelect = { viewModel.setSource(it, null) },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.padding(6.dp))
            SimpleDropdownField(
                label = "เข้าเงินสด",
                options = cashAccountOptions.ifEmpty { accountOptions },
                selected = state.destinationAccountId,
                onSelect = { viewModel.setDestination(it, null) },
                modifier = Modifier.fillMaxWidth()
            )
        }
        TransactionType.CREDIT_CARD_PAYMENT -> {
            SimpleDropdownField(
                label = "จ่ายจากบัญชี",
                options = accountOptions,
                selected = state.sourceAccountId,
                onSelect = { viewModel.setSource(it, null) },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.padding(6.dp))
            SimpleDropdownField(
                label = "ชำระบัตรเครดิต",
                options = cardOptions,
                selected = state.destinationCreditCardId,
                onSelect = { viewModel.setDestination(null, it) },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
