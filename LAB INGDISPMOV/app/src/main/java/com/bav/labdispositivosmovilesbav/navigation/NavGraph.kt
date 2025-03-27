package com.bav.labdispositivosmovilesbav.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.bav.labdispositivosmovilesbav.screens.*
import com.bav.labdispositivosmovilesbav.viewmodels.ChatViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun NavGraph(
    navController: NavHostController,
    userRole: String
) {
    Log.d("NavGraph", "Recibiendo userRole: $userRole")
    
    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {
        composable("splash") {
            SplashScreen(
                navController = navController,
                onNavigateToLogin = {
                    navController.navigate("login") {
                        popUpTo("splash") { inclusive = true }
                    }
                }
            )
        }

        composable("login") {
            LoginScreen(
                onLoginSuccess = { 
                    navController.navigate("home") {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onGoToRegister = {
                    navController.navigate("register")
                }
            )
        }

        composable("register") {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.navigate("login") {
                        popUpTo("register") { inclusive = true }
                    }
                },
                onNavigateBack = {
                    navController.navigateUp()
                }
            )
        }

        composable("home") {
            Log.d("NavGraph", "Navegando a home con rol: $userRole")
            HomeScreen(
                navController = navController,
                userRole = userRole,
                onLogout = {
                    try {
                        FirebaseAuth.getInstance().signOut()
                        navController.navigate("login") {
                            popUpTo(0) { inclusive = true }
                        }
                    } catch (e: Exception) {
                        Log.e("NavGraph", "Error en logout: ${e.message}")
                    }
                }
            )
        }

        // Ruta para la lista de usuarios (disponible para todos)
        composable("users") {
            UserListScreen(
                navController = navController,
                viewModel = viewModel<ChatViewModel>()
            )
        }

        // Ruta para el chat individual
        composable(
            route = "chat/{receiverId}",
            arguments = listOf(
                navArgument("receiverId") {
                    type = NavType.StringType
                    nullable = true
                }
            )
        ) { backStackEntry ->
            val receiverId = backStackEntry.arguments?.getString("receiverId")
            ChatScreen(
                navController = navController,
                receiverId = receiverId
            )
        }

        // Ruta de configuración solo para administradores
        composable("configuracion") {
            if (userRole == "Administrador") {
                ConfiguracionScreen(
                    userRole = userRole,
                    navController = navController
                )
            } else {
                // Redirigir a home si no es administrador
                LaunchedEffect(Unit) {
                    navController.navigate("home")
                }
            }
        }
    }
} 