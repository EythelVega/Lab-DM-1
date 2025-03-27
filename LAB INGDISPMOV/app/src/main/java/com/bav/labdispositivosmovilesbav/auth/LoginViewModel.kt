package com.bav.labdispositivosmovilesbav.auth

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class LoginUiState {
    object Initial : LoginUiState()
    object Loading : LoginUiState()
    object Success : LoginUiState()
    data class Error(val message: String) : LoginUiState()
}

class LoginViewModel : ViewModel() {
    private val authRepository = AuthRepository()
    
    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Initial)
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    private val _userRole = MutableStateFlow("Usuario")
    val userRole: StateFlow<String> = _userRole.asStateFlow()

    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                _uiState.value = LoginUiState.Loading
                Log.d("LoginViewModel", "Iniciando proceso de login")
                
                val result = authRepository.loginUser(email, password)
                result.fold(
                    onSuccess = { (user, role) -> 
                        Log.d("LoginViewModel", "Login exitoso con rol: $role")
                        _userRole.value = role
                        _uiState.value = LoginUiState.Success 
                    },
                    onFailure = { 
                        Log.e("LoginViewModel", "Error en login: ${it.message}")
                        _uiState.value = LoginUiState.Error(it.message ?: "Error desconocido") 
                    }
                )
            } catch (e: Exception) {
                Log.e("LoginViewModel", "Excepción en login: ${e.message}")
                _uiState.value = LoginUiState.Error(e.message ?: "Error desconocido")
            }
        }
    }

    fun resetState() {
        _uiState.value = LoginUiState.Initial
    }

    fun updateUserRole(role: String) {
        viewModelScope.launch {
            _userRole.value = role.trim()
            Log.d("LoginViewModel", "Rol actualizado a: ${_userRole.value}")
        }
    }
} 