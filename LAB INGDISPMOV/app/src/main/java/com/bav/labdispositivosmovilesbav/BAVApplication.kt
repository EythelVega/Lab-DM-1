package com.bav.labdispositivosmovilesbav

import android.app.Application
import android.util.Log
import com.google.firebase.FirebaseApp

class BAVApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        try {
            FirebaseApp.initializeApp(this)
            LanguageManager.setLocale(this, "es") // Idioma predeterminado
            Log.d("BAVApplication", "Firebase y configuración de idioma inicializados correctamente")
        } catch (e: Exception) {
            Log.e("BAVApplication", "Error en la inicialización: ${e.message}")
            e.printStackTrace()
        }
    }
} 