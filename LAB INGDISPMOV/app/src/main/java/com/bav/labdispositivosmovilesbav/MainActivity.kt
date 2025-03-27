package com.bav.labdispositivosmovilesbav

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.navigation.compose.rememberNavController
import com.bav.labdispositivosmovilesbav.navigation.NavGraph
import com.bav.labdispositivosmovilesbav.ui.theme.LABDispositivosMovilesBAVTheme
import com.bav.labdispositivosmovilesbav.utils.LanguageManager
import com.bav.labdispositivosmovilesbav.data.UserPreferencesRepository
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.auth.FirebaseAuth
import android.util.Log
import kotlinx.coroutines.launch
import com.google.firebase.firestore.FirebaseFirestore
import androidx.compose.runtime.mutableStateOf
import com.google.firebase.FirebaseApp
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import kotlinx.coroutines.tasks.await
import com.google.firebase.FirebaseOptions
import androidx.compose.runtime.DisposableEffect
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import android.content.Context
import android.content.res.Configuration
import kotlinx.coroutines.runBlocking
import java.util.*
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class MainActivity : ComponentActivity() {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    private var userRole = mutableStateOf("")

    override fun attachBaseContext(newBase: Context) {
        try {
            val userPreferencesRepository = UserPreferencesRepository(newBase)
            val config = Configuration(newBase.resources.configuration)
            val locale = runBlocking {
                val language = userPreferencesRepository.getLanguage()
                Locale(language)
            }
            config.setLocale(locale)
            val context = newBase.createConfigurationContext(config)
            super.attachBaseContext(context)
        } catch (e: Exception) {
            Log.e("MainActivity", "Error en attachBaseContext: ${e.message}")
            super.attachBaseContext(newBase)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        try {
            // Verificar Google Play Services
            checkGooglePlayServices()
            
            // Inicializar Firebase
            val userPreferencesRepository = UserPreferencesRepository(this)
            
            setupContent()
        } catch (e: Exception) {
            Log.e("MainActivity", "Error en onCreate: ${e.message}")
        }
    }

    private fun checkGooglePlayServices() {
        val googleApiAvailability = GoogleApiAvailability.getInstance()
        val resultCode = googleApiAvailability.isGooglePlayServicesAvailable(this)
        
        if (resultCode != ConnectionResult.SUCCESS) {
            if (googleApiAvailability.isUserResolvableError(resultCode)) {
                googleApiAvailability.getErrorDialog(this, resultCode, 9000)?.show()
            }
        }
    }

    private fun setupContent() {
        setContent {
            val navController = rememberNavController()
            var userRole = remember { mutableStateOf("Usuario") }
            val coroutineScope = rememberCoroutineScope()
            
            DisposableEffect(auth.currentUser) {
                val listener = FirebaseAuth.AuthStateListener { firebaseAuth ->
                    firebaseAuth.currentUser?.let { user ->
                        coroutineScope.launch {
                            try {
                                val document = db.collection("users")
                                    .document(user.uid)
                                    .get()
                                    .await()
                                
                                val role = document.getString("userRole") ?: "Usuario"
                                userRole.value = role
                                Log.d("MainActivity", "Rol actualizado: $role")
                            } catch (e: Exception) {
                                Log.e("MainActivity", "Error obteniendo rol: ${e.message}")
                                userRole.value = "Usuario"
                            }
                        }
                    } ?: run {
                        userRole.value = "Usuario"
                    }
                }
                
                auth.addAuthStateListener(listener)
                onDispose { auth.removeAuthStateListener(listener) }
            }

            LABDispositivosMovilesBAVTheme {
                NavGraph(
                    navController = navController,
                    userRole = userRole.value
                )
            }
        }
    }

    override fun onDestroy() {
        try {
            super.onDestroy()
        } catch (e: Exception) {
            Log.e("MainActivity", "Error en onDestroy: ${e.message}")
        }
    }
}

