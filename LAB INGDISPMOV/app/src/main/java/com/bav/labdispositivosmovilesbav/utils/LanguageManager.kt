package com.bav.labdispositivosmovilesbav.utils

import android.content.Context
import android.content.res.Configuration
import com.bav.labdispositivosmovilesbav.data.UserPreferencesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Locale

object LanguageManager {
    private lateinit var userPreferencesRepository: UserPreferencesRepository
    
    fun init(context: Context) {
        userPreferencesRepository = UserPreferencesRepository(context)
    }
    
    fun setLocale(context: Context, languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        
        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)
        
        context.createConfigurationContext(config)
        context.resources.updateConfiguration(config, context.resources.displayMetrics)
        
        // Guardar la preferencia
        CoroutineScope(Dispatchers.IO).launch {
            userPreferencesRepository.setLanguage(languageCode)
        }
    }
} 