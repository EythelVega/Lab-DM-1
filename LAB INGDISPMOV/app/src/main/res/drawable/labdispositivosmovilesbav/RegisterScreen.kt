package com.bav.labdispositivosmovilesbav
// Importaciones necesarias para que funcione el registro
import android.util.Log
import android.util.Patterns
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bav.labdispositivosmovilesbav.auth.RegisterUiState
import com.bav.labdispositivosmovilesbav.auth.RegisterViewModel

@Composable

fun RegisterScreen(
    viewModel: RegisterViewModel = viewModel(), // Maneja la lógica del registro
    onRegisterSuccess: () -> Unit  // Maneja el éxito del registro
) {
// Para almacenar los datos del usuraio
    var confirmPassword by remember { mutableStateOf("") }
    var passwordsMatch by remember { mutableStateOf(true) } // Controla si las contraseñas coinciden

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) } // Para alternar la vista de la contraseña
    var showError by remember { mutableStateOf<String?>(null) } // Mensaje de error en caso de fallo

    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState) {
        when (uiState) {
            is RegisterUiState.Success -> {
                Log.d("RegisterScreen", "Navegando a pantalla de éxito")
                onRegisterSuccess()
                viewModel.resetState()
            }
            is RegisterUiState.Error -> {
                showError = (uiState as RegisterUiState.Error).message
            }
            else -> {
                showError = null
            }
        }
    }

// Contenedor principal
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF1A237E), // Azul oscuro
                        Color(0xFF0D47A1)  // Azul más claro
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
// Texto del encabezado
            Text(
                text = "Registro",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(16.dp)),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White.copy(alpha = 0.15f)
                )
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Campo Nombre
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Nombre", color = Color.White) },
                        leadingIcon = {
                            Icon(
                                Icons.Default.Person,
                                contentDescription = "Nombre",
                                tint = Color.White
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        enabled = uiState !is RegisterUiState.Loading,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.White,
                            unfocusedBorderColor = Color.White.copy(alpha = 0.7f),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        )
                    )

                    // Campo Email
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Correo electrónico", color = Color.White) },
                        leadingIcon = {
                            Icon(
                                Icons.Default.Email,
                                contentDescription = "Email",
                                tint = Color.White
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        enabled = uiState !is RegisterUiState.Loading,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.White,
                            unfocusedBorderColor = Color.White.copy(alpha = 0.7f),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        )
                    )

                    // Campo Contraseña
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Contraseña", color = Color.White) },
                        leadingIcon = {
                            Icon(
                                Icons.Default.Lock,
                                contentDescription = "Password",
                                tint = Color.White
                            )
                        },

                        trailingIcon = {
                            IconButton(
                                onClick = { passwordVisible = !passwordVisible }
                            ) {
                                Icon(
                                    imageVector = if (passwordVisible)
                                        Icons.Default.VisibilityOff
                                    else
                                        Icons.Default.Visibility,
                                    contentDescription = if (passwordVisible)
                                        "Ocultar contraseña"
                                    else
                                        "Mostrar contraseña",
                                    tint = Color.White
                                )
                            }
                        },




                        visualTransformation = if (passwordVisible)
                            VisualTransformation.None
                        else
                            PasswordVisualTransformation(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        enabled = uiState !is RegisterUiState.Loading,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.White,
                            unfocusedBorderColor = Color.White.copy(alpha = 0.7f),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        )
                    )
                    OutlinedTextField(
                        value = confirmPassword,
                        onValueChange = {
                            confirmPassword = it
                            passwordsMatch = (password == it) // Usa 'it' para comparar con 'password'
                        },
                        label = { Text("Confirmar Contraseña", color = Color.White) },
                        leadingIcon = {
                            Icon(
                                Icons.Default.Lock,
                                contentDescription = "Confirmar contraseña",
                                tint = Color.White
                            )
                        },
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    imageVector = if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                    contentDescription = if (passwordVisible) "Ocultar contraseña" else "Mostrar contraseña",
                                    tint = Color.White
                                )
                            }
                        },
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        enabled = uiState !is RegisterUiState.Loading,
                        isError = !passwordsMatch, // Resalta el campo si no coinciden
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.White,
                            unfocusedBorderColor = Color.White.copy(alpha = 0.7f),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        )
                    )
                    if (!passwordsMatch) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp, bottom = 16.dp) // Separa del botón de registro
                                .border(1.dp, Color.Red, shape = RoundedCornerShape(8.dp)) // Borde rojo sutil
                                .background(Color(0xFFFFEBEE), shape = RoundedCornerShape(8.dp)) // Fondo rosado claro
                                .padding(12.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ErrorOutline, // Ícono menos agresivo
                                    contentDescription = "Error",
                                    tint = Color.Red,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Las contraseñas no coinciden. Inténtalo de nuevo.",
                                    color = Color(0xFFD32F2F), // Rojo elegante
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }


// Muestra mensaje de error si no coinciden

                    /*Button(
                        onClick = {
                            Log.d("RegisterScreen", "Iniciando registro con email: $email")
                            viewModel.register(name, email, password)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        enabled = name.isNotBlank() &&
                                email.isNotBlank() &&
                                password.isNotBlank() &&
                                confirmPassword.isNotBlank() &&
                                passwordsMatch &&
                                uiState !is RegisterUiState.Loading,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF2196F3),
                            disabledContainerColor = Color.Gray
                        ),
                        shape = RoundedCornerShape(25.dp)
                    ) {
                        if (uiState is RegisterUiState.Loading) {
                            CircularProgressIndicator(
                                color = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        } else {
                            Text("REGISTRARSE", fontSize = 16.sp)
                        }
                    }
                    */
                    // Botón de Registro
                    Button(
                        onClick = {
                            if (password != confirmPassword) {
                                passwordsMatch = false // Actualiza el estado para marcar error
                            } else {
                                passwordsMatch = true
                                Log.d("RegisterScreen", "Iniciando registro con email: $email")
                                viewModel.register(name, email, password)
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        enabled = name.isNotBlank() &&
                                email.isNotBlank() &&
                                password.isNotBlank() &&
                                confirmPassword.isNotBlank() &&
                                passwordsMatch && // Se debe cumplir que las contraseñas coincidan
                                uiState !is RegisterUiState.Loading,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF2196F3),
                            disabledContainerColor = Color.Gray
                        ),
                        shape = RoundedCornerShape(25.dp)
                    ) {
                        if (uiState is RegisterUiState.Loading) {
                            CircularProgressIndicator(
                                color = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        } else {
                            Text("REGISTRARSE", fontSize = 16.sp)
                        }
                    }
                }
            }

            // Mostrar errores
            AnimatedVisibility(
                visible = showError != null,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                showError?.let { error ->
                    Card(
                        modifier = Modifier
                            .padding(top = 16.dp)
                            .fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.Red.copy(alpha = 0.8f)
                        )
                    ) {
                        Text(
                            text = error,
                            color = Color.White,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }
        }
    }
}

// Mensajes de errores en caso de no poner bien
private fun validateName(name: String): String {
    return when {
        name.isEmpty() -> "El nombre es requerido"
        name.length < 3 -> "El nombre debe tener al menos 3 caracteres"
        else -> ""
    }
}

private fun validateEmail(email: String): String {
    return when {
        email.isEmpty() -> "El correo electrónico es requerido"
        !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> "Correo electrónico inválido"
        else -> ""
    }
}

private fun validatePassword(password: String): String {
    return when {
        password.isEmpty() -> "La contraseña es requerida"
        password.length < 6 -> "La contraseña debe tener al menos 6 caracteres"
        !password.any { it.isDigit() } -> "La contraseña debe contener al menos un número"
        !password.any { it.isUpperCase() } -> "La contraseña debe contener al menos una mayúscula"
        else -> ""
    }
}

private fun isFormValid(name: String, email: String, password: String): Boolean {
    return validateName(name).isEmpty() &&
            validateEmail(email).isEmpty() &&
            validatePassword(password).isEmpty()
}

private fun validateConfirmPassword(password: String, confirmPassword: String): String {
    return when {
        confirmPassword.isEmpty() -> "Debe confirmar la contraseña"
        confirmPassword != password -> "Las contraseñas no coinciden"
        else -> ""
    }
}
private fun isFormValid(name: String, email: String, password: String, confirmPassword: String): Boolean {
    return validateName(name).isEmpty() &&
            validateEmail(email).isEmpty() &&
            validatePassword(password).isEmpty() &&
            validateConfirmPassword(password, confirmPassword).isEmpty()
}
