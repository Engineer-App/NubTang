package com.pft.tracker.ui.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

data class DropdownOption<T>(val value: T, val label: String)

/**
 * A dropdown selector built from a plain OutlinedTextField + click-overlay +
 * DropdownMenu, avoiding ExposedDropdownMenuBox (its API shape has shifted
 * across compose-material3 releases and isn't worth pinning exactly here).
 */
@Composable
fun <T> SimpleDropdownField(
    label: String,
    options: List<DropdownOption<T>>,
    selected: T?,
    onSelect: (T?) -> Unit,
    placeholder: String = "เลือก",
    allowClear: Boolean = false,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    var expanded by remember { mutableStateOf(false) }
    val selectedLabel = options.find { it.value == selected }?.label ?: placeholder

    Box(modifier = modifier) {
        OutlinedTextField(
            value = selectedLabel,
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = { Icon(Icons.Filled.ArrowDropDown, contentDescription = null) },
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium,
            enabled = enabled
        )
        if (enabled) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .clickable { expanded = true }
            )
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            if (allowClear) {
                DropdownMenuItem(text = { Text(placeholder) }, onClick = { onSelect(null); expanded = false })
            }
            options.forEach { opt ->
                DropdownMenuItem(text = { Text(opt.label) }, onClick = { onSelect(opt.value); expanded = false })
            }
        }
    }
}
