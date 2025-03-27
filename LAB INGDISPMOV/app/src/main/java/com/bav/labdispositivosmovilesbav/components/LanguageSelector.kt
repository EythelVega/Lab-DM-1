package com.bav.labdispositivosmovilesbav.components

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Language
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.bav.labdispositivosmovilesbav.data.UserPreferencesRepository
import com.bav.labdispositivosmovilesbav.utils.LanguageManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanguageSelector(
    userRole: String,
    currentLanguage: String,
    onLanguageSelected: (String) -> Unit,
    userPreferencesRepository: UserPreferencesRepository
) {
    val context = LocalContext.current
    val languages = userPreferencesRepository.getAvailableLanguages(userRole)

    languages.forEach { language ->
        DropdownMenuItem(
            text = { Text(userPreferencesRepository.getLanguageDisplayName(language)) },
            onClick = { onLanguageSelected(language) }
        )
    }
} 