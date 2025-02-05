package com.bav.labdispositivosmovilesbav.auth

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RegisterViewModel : ViewModel() {
    init {
        Log.d("RegisterViewModel", "ViewModel initialized")
    }
    
    private val authRepository = AuthRepository()
    
    private val _uiState = MutableStateFlow<RegisterUiState>(RegisterUiState.Initial)
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    fun register(name: String, email: String, password: String) {
        viewModelScope.launch {
            try {
                _uiState.value = RegisterUiState.Loading
                Log.d("RegisterViewModel", "Iniciando registro")
                
                val result = authRepository.registerUser(name, email, password)
                result.fold(
                    onSuccess = { 
                        Log.d("RegisterViewModel", "Registro exitoso")
                        _uiState.value = RegisterUiState.Success 
                    },
                    onFailure = { 
                        Log.e("RegisterViewModel", "Error en registro: ${it.message}")
                        _uiState.value = RegisterUiState.Error(it.message ?: "Error desconocido") 
                    }
                )
            } catch (e: Exception) {
                Log.e("RegisterViewModel", "Excepción en registro: ${e.message}")
                _uiState.value = RegisterUiState.Error(e.message ?: "Error desconocido")
            }
        }
    }

    fun resetState() {
        _uiState.value = RegisterUiState.Initial
    }
}

sealed class RegisterUiState {
    object Initial : RegisterUiState()
    object Loading : RegisterUiState()
    object Success : RegisterUiState()
    data class Error(val message: String) : RegisterUiState()
} 