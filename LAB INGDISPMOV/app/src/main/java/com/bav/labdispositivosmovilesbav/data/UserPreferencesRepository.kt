package com.bav.labdispositivosmovilesbav.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.first
import com.bav.labdispositivosmovilesbav.R

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class UserPreferencesRepository(private val context: Context) {
    private val LANGUAGE_KEY = stringPreferencesKey("language")
    
    val language: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[LANGUAGE_KEY] ?: "es"
    }

    suspend fun getLanguage(): String {
        return context.dataStore.data.map { preferences ->
            preferences[LANGUAGE_KEY] ?: "es"
        }.first()
    }

    suspend fun setLanguage(languageCode: String) {
        context.dataStore.edit { preferences ->
            preferences[LANGUAGE_KEY] = languageCode
        }
    }

    fun getAvailableLanguages(userRole: String): List<String> {
        val languageCodes = context.resources.getStringArray(R.array.language_codes).toList()
        return when (userRole) {
            "Administrador" -> languageCodes
            else -> languageCodes.take(2)
        }
    }

    fun getLanguageDisplayName(languageCode: String): String {
        val languageCodes = context.resources.getStringArray(R.array.language_codes)
        val languageNames = context.resources.getStringArray(R.array.language_names)
        val index = languageCodes.indexOf(languageCode)
        return if (index >= 0) languageNames[index] else languageCode
    }

    fun getStringForLanguage(resourceId: Int, languageCode: String): String {
        val resourceName = context.resources.getResourceEntryName(resourceId)
        val languageResourceId = context.resources.getIdentifier(
            "${resourceName}_$languageCode",
            "string",
            context.packageName
        )
        return try {
            context.getString(languageResourceId)
        } catch (e: Exception) {
            context.getString(resourceId)
        }
    }
} 