package com.bav.labdispositivosmovilesbav

import android.app.Application
import android.util.Log
import com.google.firebase.FirebaseApp

class BAVApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        try {
            FirebaseApp.initializeApp(this)
            Log.d("BAVApplication", "Firebase initialized successfully")
        } catch (e: Exception) {
            Log.e("BAVApplication", "Error initializing Firebase: ${e.message}")
            e.printStackTrace()
        }
    }
} 