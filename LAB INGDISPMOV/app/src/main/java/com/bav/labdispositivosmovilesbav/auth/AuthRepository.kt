package com.bav.labdispositivosmovilesbav.auth

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class AuthRepository {
    private val auth: FirebaseAuth by lazy {
        try {
            FirebaseAuth.getInstance().also {
                Log.d("AuthRepository", "FirebaseAuth inicializado correctamente")
            }
        } catch (e: Exception) {
            Log.e("AuthRepository", "Error al inicializar FirebaseAuth: ${e.message}")
            throw e
        }
    }

    private val firestore: FirebaseFirestore by lazy {
        try {
            FirebaseFirestore.getInstance().also {
                Log.d("AuthRepository", "Firestore inicializado correctamente")
            }
        } catch (e: Exception) {
            Log.e("AuthRepository", "Error al inicializar Firestore: ${e.message}")
            throw e
        }
    }

    suspend fun registerUser(name: String, email: String, password: String): Result<FirebaseUser> {
        return try {
            Log.d("AuthRepository", "Iniciando registro de usuario: $email")
            val authResult = auth.createUserWithEmailAndPassword(email, password).await()
            
            authResult.user?.let { user ->
                Log.d("AuthRepository", "Usuario creado exitosamente: ${user.uid}")
                // Guardar información adicional en Firestore
                val userInfo = hashMapOf(
                    "name" to name,
                    "email" to email
                )
                
                try {
                    firestore.collection("users")
                        .document(user.uid)
                        .set(userInfo)
                        .await()
                    Log.d("AuthRepository", "Información de usuario guardada en Firestore")
                    Result.success(user)
                } catch (e: Exception) {
                    Log.e("AuthRepository", "Error al guardar en Firestore: ${e.message}")
                    Result.failure(e)
                }
            } ?: Result.failure(Exception("Error: Usuario no creado"))
            
        } catch (e: Exception) {
            Log.e("AuthRepository", "Error en el registro: ${e.message}")
            when {
                e.message?.contains("CONFIGURATION_NOT_FOUND") == true -> 
                    Result.failure(Exception("Error de configuración de Firebase. Por favor, verifica la configuración de autenticación."))
                e.message?.contains("EMAIL_EXISTS") == true ->
                    Result.failure(Exception("El correo electrónico ya está registrado"))
                e.message?.contains("INVALID_EMAIL") == true ->
                    Result.failure(Exception("El formato del correo electrónico no es válido"))
                e.message?.contains("WEAK_PASSWORD") == true ->
                    Result.failure(Exception("La contraseña es muy débil"))
                else -> Result.failure(Exception("Error en el registro: ${e.message}"))
            }
        }
    }
} 