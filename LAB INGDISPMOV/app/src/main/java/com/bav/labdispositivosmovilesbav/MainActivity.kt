package com.bav.labdispositivosmovilesbav

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.compose.rememberNavController
import com.bav.labdispositivosmovilesbav.data.UserPreferencesRepository
import com.bav.labdispositivosmovilesbav.navigation.NavGraph
import com.bav.labdispositivosmovilesbav.ui.theme.LABDispositivosMovilesBAVTheme
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import io.agora.rtc.IRtcEngineEventHandler
import io.agora.rtc.RtcEngine
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import java.util.Locale

class MainActivity : ComponentActivity() {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    private var userRole = mutableStateOf("")

    // Initialize RtcEngine variable
    private lateinit var rtcEngine: RtcEngine
    private val agoraAppId = "753cc0ad24584a4985b5b4b840fc6560" // Reemplázalo con tu App ID de Agora

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
            checkGooglePlayServices()
            initializeAgoraEngine()  // Initialize Agora RTC Engine
            setupContent()  // Set up the UI content
        } catch (e: Exception) {
            Log.e("MainActivity", "Error en onCreate: ${e.message}")
        }
    }
    private fun initializeAgoraEngine() {
        try {
            rtcEngine = RtcEngine.create(this, agoraAppId, object : IRtcEngineEventHandler() {
                override fun onJoinChannelSuccess(channel: String?, uid: Int, elapsed: Int) {
                    Log.d("Agora", "Unido a canal: $channel con UID: $uid")
                }

                override fun onUserJoined(uid: Int, elapsed: Int) {
                    Log.d("Agora", "Usuario unido: $uid")
                }
            })

            rtcEngine.setLogFile(getExternalFilesDir(null)?.absolutePath + "/agora_rtc.log")
            Log.d("Agora", "Agora RTC Engine inicializado correctamente")

        } catch (e: Exception) {
            Log.e("Agora", "Error al inicializar Agora RTC Engine: ${e.message}")
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

    // Initialize Agora RTC Engine



    // Set up UI content
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
                    userRole = userRole.value,
                    rtcEngine = rtcEngine  // Pass the RtcEngine to NavGraph
                )
            }
        }
    }

    override fun onDestroy() {
        try {
            RtcEngine.destroy()  // Properly destroy Agora RTC Engine
            Log.d("Agora", "Agora RTC Engine destruido")
            super.onDestroy()
        } catch (e: Exception) {
            Log.e("MainActivity", "Error en onDestroy: ${e.message}")
        }
    }
}
