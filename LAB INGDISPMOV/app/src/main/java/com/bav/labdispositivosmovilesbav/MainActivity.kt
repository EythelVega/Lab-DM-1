package com.bav.labdispositivosmovilesbav

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.compose.rememberNavController
import com.bav.labdispositivosmovilesbav.navigation.NavGraph
import com.bav.labdispositivosmovilesbav.ui.theme.LABDispositivosMovilesBAVTheme
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class MainActivity : ComponentActivity() {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    private var userRole = mutableStateOf("")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {
            checkGooglePlayServices()
            setupContent()  // Set up the UI content
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
                    userRole = userRole.value
                )
            }
        }
    }
}
