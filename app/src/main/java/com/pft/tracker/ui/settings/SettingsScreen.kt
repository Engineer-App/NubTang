package com.pft.tracker.ui.settings

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.pft.tracker.di.AppContainer
import com.pft.tracker.di.ViewModelFactory
import com.pft.tracker.security.BiometricAuthHelper
import com.pft.tracker.ui.common.DropdownOption
import com.pft.tracker.ui.common.SimpleDropdownField
import com.pft.tracker.ui.nav.Routes
import com.pft.tracker.util.UpdateStatus
import com.pft.tracker.BuildConfig
import kotlinx.coroutines.launch
import java.time.LocalDate

@Composable
fun SettingsScreen(container: AppContainer, navController: NavController) {
    val viewModel: SettingsViewModel = viewModel(factory = ViewModelFactory { SettingsViewModel(container) })
    val state by viewModel.uiState.collectAsState()
    val updateStatus by viewModel.updateStatus.collectAsState()
    val context = LocalContext.current
    val activity = context as? FragmentActivity
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    var showChangePinDialog by remember { mutableStateOf(false) }
    var showAboutDialog by remember { mutableStateOf(false) }
    var showResetDialog by remember { mutableStateOf(false) }
    var showRestoreConfirm by remember { mutableStateOf(false) }
    var pendingRestoreUri by remember { mutableStateOf<android.net.Uri?>(null) }

    val backupLauncher = rememberLauncherForActivityResult(ActivityResultContracts.CreateDocument("application/octet-stream")) { uri ->
        if (uri != null) {
            scope.launch {
                val bytes = viewModel.exportBackup()
                runCatching {
                    context.contentResolver.openOutputStream(uri)?.use { it.write(bytes) }
                }
                snackbarHostState.showSnackbar("สำรองข้อมูลเรียบร้อย")
            }
        }
    }

    val restoreLauncher = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        if (uri != null) {
            pendingRestoreUri = uri
            showRestoreConfirm = true
        }
    }

    val csvLauncher = rememberLauncherForActivityResult(ActivityResultContracts.CreateDocument("text/csv")) { uri ->
        if (uri != null) {
            scope.launch {
                val csv = viewModel.exportCsv()
                runCatching {
                    context.contentResolver.openOutputStream(uri)?.use { it.write(csv.toByteArray(Charsets.UTF_8)) }
                }
                snackbarHostState.showSnackbar("ส่งออก CSV เรียบร้อย")
            }
        }
    }

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("ตั้งค่า", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.padding(padding),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { SectionHeader("ความปลอดภัย") }
            item {
                SettingsRow(title = "เปลี่ยนรหัส PIN", onClick = { showChangePinDialog = true })
            }
            item {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("ใช้ลายนิ้วมือ/ใบหน้า")
                        Switch(
                            checked = state.biometricEnabled,
                            onCheckedChange = { enabled ->
                                if (enabled && activity != null && !BiometricAuthHelper.canAuthenticate(activity)) {
                                    scope.launch { snackbarHostState.showSnackbar("อุปกรณ์นี้ไม่รองรับหรือยังไม่ตั้งค่าไบโอเมตริก") }
                                } else {
                                    viewModel.setBiometricEnabled(enabled)
                                }
                            }
                        )
                    }
                }
            }
            item {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(16.dp)) {
                        Text("ระยะเวลาก่อนล็อกอัตโนมัติ", style = MaterialTheme.typography.bodyMedium)
                        SimpleDropdownField(
                            label = "นาที",
                            options = listOf(1, 5, 15, 30).map { DropdownOption(it, "$it นาที") },
                            selected = state.autoLockMinutes,
                            onSelect = { it?.let(viewModel::setAutoLockMinutes) },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            item { SectionHeader("หมวดหมู่และแผนการเงิน") }
            item { SettingsRow("หมวดหมู่และงบประมาณ") { navController.navigate(Routes.CATEGORIES) } }
            item { SettingsRow("รายการล่วงหน้า/ผ่อนชำระ") { navController.navigate(Routes.RECURRING) } }

            item { SectionHeader("ข้อมูล") }
            item {
                SettingsRow("สำรองข้อมูล (เข้ารหัส)") {
                    backupLauncher.launch("pft_backup_${LocalDate.now()}.pftbackup")
                }
            }
            item {
                SettingsRow("กู้คืนข้อมูล") {
                    restoreLauncher.launch(arrayOf("*/*"))
                }
            }
            item {
                SettingsRow("ส่งออก CSV") {
                    csvLauncher.launch("transactions_${LocalDate.now()}.csv")
                }
            }

            item { SectionHeader("อื่นๆ") }
            item { SettingsRow("รีเซ็ตข้อมูลรายการ") { showResetDialog = true } }
            item { SettingsRow("เกี่ยวกับแอป") { showAboutDialog = true } }
        }
    }

    if (showChangePinDialog) {
        ChangePinDialog(
            onDismiss = { showChangePinDialog = false },
            onSubmit = { current, new, callback ->
                viewModel.changePin(current, new) { success, error ->
                    callback(success, error)
                    if (success) showChangePinDialog = false
                }
            }
        )
    }

    if (showAboutDialog) {
        AboutDialog(
            versionName = BuildConfig.VERSION_NAME,
            updateStatus = updateStatus,
            onDismiss = { showAboutDialog = false },
            onCheckUpdate = { viewModel.checkForUpdates() },
            onDownloadUpdate = { viewModel.downloadUpdate(it) }
        )
    }

    if (showRestoreConfirm) {
        AlertDialog(
            onDismissRequest = { showRestoreConfirm = false },
            title = { Text("กู้คืนข้อมูล?") },
            text = { Text("ข้อมูลปัจจุบันทั้งหมดจะถูกแทนที่ด้วยข้อมูลจากไฟล์สำรอง การกระทำนี้ย้อนกลับไม่ได้") },
            confirmButton = {
                TextButton(onClick = {
                    showRestoreConfirm = false
                    val uri = pendingRestoreUri
                    if (uri != null) {
                        scope.launch {
                            runCatching {
                                val bytes = context.contentResolver.openInputStream(uri)?.use { it.readBytes() }
                                    ?: throw IllegalStateException("อ่านไฟล์ไม่ได้")
                                viewModel.importBackup(bytes)
                            }.onSuccess {
                                snackbarHostState.showSnackbar("กู้คืนข้อมูลเรียบร้อย")
                            }.onFailure {
                                snackbarHostState.showSnackbar("กู้คืนข้อมูลไม่สำเร็จ: ${it.message}")
                            }
                        }
                    }
                }) { Text("กู้คืน") }
            },
            dismissButton = {
                TextButton(onClick = { showRestoreConfirm = false }) { Text("ยกเลิก") }
            }
        )
    }

    if (showResetDialog) {
        var mode by remember { mutableStateOf("MONTH") } // "MONTH" or "ALL"
        var selectedYear by remember { mutableStateOf(java.time.LocalDate.now().year) }
        var selectedMonth by remember { mutableStateOf(java.time.LocalDate.now().monthValue) }
        var showConfirm by remember { mutableStateOf(false) }

        AlertDialog(
            onDismissRequest = { showResetDialog = false },
            title = { Text("รีเซ็ตข้อมูลรายการ") },
            text = {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(selected = mode == "MONTH", onClick = { mode = "MONTH" })
                        Text("รายเดือน")
                        Spacer(Modifier.padding(horizontal = 8.dp))
                        RadioButton(selected = mode == "ALL", onClick = { mode = "ALL" })
                        Text("ลบทั้งหมด")
                    }
                    if (mode == "MONTH") {
                        Spacer(Modifier.padding(8.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            SimpleDropdownField(
                                label = "เดือน",
                                options = (1..12).map { DropdownOption(it, com.pft.tracker.ui.common.monthLabel(2024, it).split(" ")[0]) },
                                selected = selectedMonth,
                                onSelect = { it?.let { selectedMonth = it } },
                                modifier = Modifier.weight(1.5f)
                            )
                            Spacer(Modifier.padding(horizontal = 4.dp))
                            SimpleDropdownField(
                                label = "ปี",
                                options = (2020..2030).map { DropdownOption(it, (it + 543).toString()) },
                                selected = selectedYear,
                                onSelect = { it?.let { selectedYear = it } },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showConfirm = true }) { Text("ดำเนินการ") }
            },
            dismissButton = {
                TextButton(onClick = { showResetDialog = false }) { Text("ยกเลิก") }
            }
        )

        if (showConfirm) {
            AlertDialog(
                onDismissRequest = { showConfirm = false },
                title = { Text("ยืนยันการลบ") },
                text = { Text("ข้อมูลรายการใน${if (mode == "MONTH") com.pft.tracker.ui.common.monthLabel(selectedYear, selectedMonth) else "ทั้งหมด"} จะถูกลบถาวร ยืนยันหรือไม่?") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            if (mode == "ALL") viewModel.resetAllData() else viewModel.resetMonthData(selectedYear, selectedMonth)
                            showConfirm = false
                            showResetDialog = false
                            scope.launch { snackbarHostState.showSnackbar("ลบข้อมูลเรียบร้อย") }
                        },
                        colors = androidx.compose.material3.ButtonDefaults.textButtonColors(contentColor = com.pft.tracker.ui.theme.BudgetOverColor)
                    ) { Text("ลบถาวร") }
                },
                dismissButton = {
                    TextButton(onClick = { showConfirm = false }) { Text("ยกเลิก") }
                }
            )
        }
    }
}

@Composable
private fun SectionHeader(title: String) {
    Text(title, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
}

@Composable
private fun SettingsRow(title: String, onClick: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth().clickable(onClick = onClick)) {
        Text(title, modifier = Modifier.fillMaxWidth().padding(16.dp), style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
private fun ChangePinDialog(
    onDismiss: () -> Unit,
    onSubmit: (String, String, (Boolean, String?) -> Unit) -> Unit
) {
    var current by remember { mutableStateOf("") }
    var new by remember { mutableStateOf("") }
    var confirm by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("เปลี่ยนรหัส PIN") },
        text = {
            Column {
                OutlinedTextField(value = current, onValueChange = { current = it.filter(Char::isDigit) }, label = { Text("PIN ปัจจุบัน") })
                OutlinedTextField(value = new, onValueChange = { new = it.filter(Char::isDigit) }, label = { Text("PIN ใหม่") })
                OutlinedTextField(value = confirm, onValueChange = { confirm = it.filter(Char::isDigit) }, label = { Text("ยืนยัน PIN ใหม่") })
                if (error != null) Text(error!!, color = MaterialTheme.colorScheme.error)
            }
        },
        confirmButton = {
            TextButton(onClick = {
                if (new != confirm) {
                    error = "PIN ใหม่ไม่ตรงกัน"
                } else {
                    onSubmit(current, new) { success, err -> if (!success) error = err }
                }
            }) { Text("บันทึก") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("ยกเลิก") }
        }
    )
}

@Composable
private fun AboutDialog(
    versionName: String,
    updateStatus: UpdateStatus,
    onDismiss: () -> Unit,
    onCheckUpdate: () -> Unit,
    onDownloadUpdate: (com.pft.tracker.util.UpdateInfo) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("เกี่ยวกับแอป") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("NabTang (นับตังค์)", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text("เวอร์ชัน $versionName", style = MaterialTheme.typography.bodyMedium)
                
                Spacer(Modifier.height(8.dp))
                Text(
                    "นโยบายความเป็นส่วนตัว:",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    "ข้อมูลทางการเงินทั้งหมดของคุณจะถูกเก็บไว้ภายในเครื่องนี้เท่านั้น ไม่มีการส่งข้อมูลไปยังเซิร์ฟเวอร์ภายนอก เพื่อความปลอดภัยสูงสุดของคุณ",
                    style = MaterialTheme.typography.bodySmall
                )
                
                Spacer(Modifier.height(16.dp))
                Divider()
                Spacer(Modifier.height(8.dp))
                
                when (updateStatus) {
                    is UpdateStatus.Idle -> {
                        TextButton(onClick = onCheckUpdate, modifier = Modifier.fillMaxWidth()) {
                            Text("ตรวจสอบการอัปเดต")
                        }
                    }
                    is UpdateStatus.Checking -> {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
                            androidx.compose.material3.CircularProgressIndicator(modifier = Modifier.size(16.dp), strokeWidth = 2.dp)
                            Spacer(Modifier.width(8.dp))
                            Text("กำลังตรวจสอบ...", style = MaterialTheme.typography.bodySmall)
                        }
                    }
                    is UpdateStatus.NewVersionAvailable -> {
                        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                            Text("พบเวอร์ชันใหม่: ${updateStatus.info.latestVersionName}", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                            if (updateStatus.info.releaseNotes.isNotEmpty()) {
                                Text(updateStatus.info.releaseNotes, style = MaterialTheme.typography.bodySmall)
                            }
                            Spacer(Modifier.height(8.dp))
                            androidx.compose.material3.Button(onClick = { onDownloadUpdate(updateStatus.info) }) {
                                Text("ดาวน์โหลดและติดตั้ง")
                            }
                        }
                    }
                    is UpdateStatus.UpToDate -> {
                        Text("แอปของคุณเป็นเวอร์ชันล่าสุดแล้ว", color = Color.Gray, style = MaterialTheme.typography.bodySmall, modifier = Modifier.fillMaxWidth(), textAlign = androidx.compose.ui.text.style.TextAlign.Center)
                    }
                    is UpdateStatus.Downloading -> {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            androidx.compose.material3.LinearProgressIndicator(
                                progress = updateStatus.progress,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Text("กำลังดาวน์โหลด... ${(updateStatus.progress * 100).toInt()}%", style = MaterialTheme.typography.bodySmall)
                        }
                    }
                    is UpdateStatus.Error -> {
                        Text(updateStatus.message, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
                        TextButton(onClick = onCheckUpdate) { Text("ลองใหม่") }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) { Text("ปิด") }
        }
    )
}

@Composable
private fun Divider() {
    androidx.compose.material3.HorizontalDivider(
        color = MaterialTheme.colorScheme.outlineVariant,
        thickness = 1.dp
    )
}
