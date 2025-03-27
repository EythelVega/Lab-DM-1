package com.bav.labdispositivosmovilesbav.utils

import android.content.Context
import android.content.res.Configuration
import com.bav.labdispositivosmovilesbav.data.UserPreferencesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Locale
import android.app.Activity
import android.util.Log
import android.content.Intent

object LanguageManager {
    private var userPreferencesRepository: UserPreferencesRepository? = null
    
    fun setLocale(context: Context, languageCode: String) {
        try {
            // Inicializar el repositorio si aún no existe
            if (userPreferencesRepository == null) {
                userPreferencesRepository = UserPreferencesRepository(context)
            }
            
            val locale = Locale(languageCode)
            Locale.setDefault(locale)
            
            val config = Configuration(context.resources.configuration)
            config.setLocale(locale)
            
            context.createConfigurationContext(config)
            context.resources.updateConfiguration(config, context.resources.displayMetrics)
            
            // Guardar la preferencia de manera segura
            userPreferencesRepository?.let { repository ->
                CoroutineScope(Dispatchers.IO).launch {
                    repository.setLanguage(languageCode)
                }
            }

            // Reiniciar la actividad manteniendo el estado
            if (context is Activity) {
                val intent = context.intent
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                context.finish()
                context.startActivity(intent)
                context.overridePendingTransition(0, 0)
            }
            
            Log.d("LanguageManager", "Idioma cambiado exitosamente a: $languageCode")
        } catch (e: Exception) {
            Log.e("LanguageManager", "Error en setLocale: ${e.message}")
        }
    }
} 