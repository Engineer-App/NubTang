package com.pft.tracker.ui.lock

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Backspace
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity
import com.pft.tracker.security.BiometricAuthHelper

@Composable
fun LockScreen(viewModel: LockViewModel) {
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current
    val activity = context as? FragmentActivity

    LaunchedEffect(state.requestBiometricTick) {
        if (state.requestBiometricTick > 0 && activity != null) {
            Log.d("LockScreen", "Showing biometric prompt, tick: ${state.requestBiometricTick}")
            BiometricAuthHelper.showPrompt(
                activity,
                onSuccess = { 
                    Log.d("LockScreen", "Biometric success")
                    viewModel.onBiometricSuccess() 
                },
                onError = { err -> 
                    Log.d("LockScreen", "Biometric error/cancel: $err")
                    /* fall back to PIN entry silently */ 
                }
            )
        }
    }

    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        Column(
            modifier = Modifier.fillMaxSize().padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                Icons.Filled.Lock,
                contentDescription = null,
                modifier = Modifier.size(48.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(Modifier.height(16.dp))
            Text(
                text = when (state.mode) {
                    LockMode.SETUP_NEW_PIN -> "ตั้งรหัส PIN (4 หลัก)"
                    LockMode.SETUP_CONFIRM_PIN -> "ยืนยันรหัส PIN อีกครั้ง"
                    else -> "กรอกรหัส PIN"
                },
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(24.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                repeat(4) { index ->
                    val filled = index < state.pinInput.length
                    Box(
                        modifier = Modifier
                            .size(16.dp)
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = if (filled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier.fillMaxSize()
                        ) {}
                    }
                }
            }

            Spacer(Modifier.height(12.dp))
            Text(
                text = state.error ?: " ",
                color = Color.Red,
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(Modifier.height(24.dp))

            NumberPad(
                onDigit = { 
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    viewModel.onDigit(it) 
                },
                onBackspace = {
                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    viewModel.onBackspace()
                },
                showBiometric = state.mode == LockMode.ENTER_PIN && state.biometricEnabled,
                onBiometric = { viewModel.triggerBiometric() }
            )
        }
    }
}

@Composable
private fun NumberPad(
    onDigit: (Char) -> Unit,
    onBackspace: () -> Unit,
    showBiometric: Boolean,
    onBiometric: () -> Unit
) {
    val rows = listOf(
        listOf('1', '2', '3'),
        listOf('4', '5', '6'),
        listOf('7', '8', '9')
    )
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        rows.forEach { row ->
            Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                row.forEach { digit ->
                    DigitButton(digit.toString()) { onDigit(digit) }
                }
            }
            Spacer(Modifier.height(12.dp))
        }
        Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
            Box(modifier = Modifier.size(56.dp), contentAlignment = Alignment.Center) {
                if (showBiometric) {
                    IconButton(onClick = onBiometric) {
                        Icon(Icons.Filled.Fingerprint, contentDescription = "ลายนิ้วมือ")
                    }
                }
            }
            DigitButton("0") { onDigit('0') }
            Box(modifier = Modifier.size(56.dp), contentAlignment = Alignment.Center) {
                IconButton(onClick = onBackspace) {
                    Icon(Icons.Filled.Backspace, contentDescription = "ลบ")
                }
            }
        }
    }
}

@Composable
private fun DigitButton(label: String, onClick: () -> Unit) {
    Surface(
        shape = CircleShape,
        color = MaterialTheme.colorScheme.surfaceVariant,
        modifier = Modifier
            .size(56.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            androidx.compose.material3.TextButton(onClick = onClick, modifier = Modifier.fillMaxSize()) {
                Text(label, style = MaterialTheme.typography.titleLarge)
            }
        }
    }
}
