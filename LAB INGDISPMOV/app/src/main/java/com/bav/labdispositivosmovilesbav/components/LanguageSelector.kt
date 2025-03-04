package com.bav.labdispositivosmovilesbav.components

import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Language
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.bav.labdispositivosmovilesbav.R
import com.bav.labdispositivosmovilesbav.utils.LanguageManager

@Composable
fun LanguageSelector() {
    var expanded by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Box {
        IconButton(onClick = { expanded = true }) {
            Icon(
                imageVector = Icons.Default.Language,
                contentDescription = stringResource(R.string.change_language)
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text(stringResource(R.string.language_es)) },
                onClick = {
                    LanguageManager.setLocale(context, "es")
                    expanded = false
                }
            )
            DropdownMenuItem(
                text = { Text(stringResource(R.string.language_en)) },
                onClick = {
                    LanguageManager.setLocale(context, "en")
                    expanded = false
                }
            )
        }
    }
} 