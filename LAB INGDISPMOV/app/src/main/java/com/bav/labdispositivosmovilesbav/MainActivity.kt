package com.bav.labdispositivosmovilesbav

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.bav.labdispositivosmovilesbav.ui.theme.LABDispositivosMovilesBAVTheme
import com.bav.labdispositivosmovilesbav.utils.LanguageManager
import com.bav.labdispositivosmovilesbav.data.UserPreferencesRepository
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.auth.FirebaseAuth
import android.util.Log
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializar LanguageManager
        LanguageManager.init(applicationContext)

        // Restaurar el idioma guardado
        val userPreferencesRepository = UserPreferencesRepository(applicationContext)
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                userPreferencesRepository.language.collect { language ->
                    LanguageManager.setLocale(this@MainActivity, language)
                }
            }
        }

        // Recuperar rol del usuario desde Firebase
        val db = Firebase.firestore
        val userId = FirebaseAuth.getInstance().currentUser?.uid

        if (userId != null) {
            db.collection("users").document(userId).get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        val userRole = document.getString("userRole") ?: "Usuario" // Rol predeterminado
                        Log.d("UserRole", "El rol del usuario es: $userRole")

                        // Configurar la interfaz con el rol del usuario
                        setContent {
                            LABDispositivosMovilesBAVTheme {
                                NavGraph(startDestination = "home", userRole = userRole) // ✅ Ajustamos para pasar el rol
                            }
                        }
                    }
                }
                .addOnFailureListener { exception ->
                    Log.e("UserRole", "Error obteniendo el rol del usuario: ${exception.message}")
                }
        }
    }
}

