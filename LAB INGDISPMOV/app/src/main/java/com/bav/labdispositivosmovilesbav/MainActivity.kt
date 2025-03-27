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

class MainActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
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
            auth = FirebaseAuth.getInstance()
            db = FirebaseFirestore.getInstance()

            // Configurar el manejo de cambios de idioma
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
            var currentUserRole by remember { userRole }
            
            // Observar cambios en el usuario actual
            LaunchedEffect(auth.currentUser) {
                val currentUser = auth.currentUser
                if (currentUser != null) {
                    try {
                        val userDoc = db.collection("users")
                            .document(currentUser.uid)
                            .get()
                            .await()
                        val role = userDoc.getString("userRole") ?: "Usuario"
                        Log.d("MainActivity", "Rol obtenido en MainActivity: $role")
                        currentUserRole = role
                        Log.d("MainActivity", "Rol actualizado en MainActivity: $currentUserRole")
                    } catch (e: Exception) {
                        Log.e("MainActivity", "Error obteniendo rol: ${e.message}")
                        currentUserRole = "Usuario"
                    }
                } else {
                    currentUserRole = ""
                }
            }

            LABDispositivosMovilesBAVTheme {
                Log.d("MainActivity", "Pasando rol a NavGraph: $currentUserRole")
                NavGraph(
                    navController = navController,
                    userRole = currentUserRole
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

