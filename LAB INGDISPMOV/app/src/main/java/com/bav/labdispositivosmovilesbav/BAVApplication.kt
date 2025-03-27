package com.bav.labdispositivosmovilesbav

import android.app.Application
import android.util.Log
import com.google.firebase.FirebaseApp
import com.bav.labdispositivosmovilesbav.utils.LanguageManager
import com.bav.labdispositivosmovilesbav.data.UserPreferencesRepository
import com.google.firebase.FirebaseOptions

class BAVApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        
        try {
            // Inicializar Firebase con opciones explícitas
            val options = FirebaseOptions.Builder()
                .setApplicationId(getString(R.string.google_app_id))
                .setApiKey(getString(R.string.google_api_key))
                .setProjectId(getString(R.string.project_id))
                .build()

            if (FirebaseApp.getApps(this).isEmpty()) {
                FirebaseApp.initializeApp(this, options)
                Log.d("BAVApplication", "Firebase inicializado correctamente")
            }

            // Inicializar otros componentes
            val userPreferencesRepository = UserPreferencesRepository(this)
            LanguageManager.setLocale(this, "es")
            
        } catch (e: Exception) {
            Log.e("BAVApplication", "Error inicializando Firebase: ${e.message}")
            e.printStackTrace()
        }
    }
}
            