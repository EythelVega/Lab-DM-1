package com.bav.labdispositivosmovilesbav

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun NavGraph(startDestination: String = "splash", userRole: String) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // Pantalla de carga inicial
        composable("splash") {
            SplashScreen(navController)
        }

        // Pantalla de inicio/login
        composable("login") {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onGoToRegister = {
                    navController.navigate("register")
                }
            )
        }

        // Pantalla de registro
        composable("register") {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.navigate("success") {
                        popUpTo("register") { inclusive = true }
                    }
                }
            )
        }

        // Pantalla de éxito
        composable("success") {
            SuccessScreen {
                navController.navigate("login") {
                    popUpTo("success") { inclusive = true }
                }
            }
        }

        // Pantalla principal (home)
        composable("home") {
            HomeScreen(
                navController = navController,
                userRole = userRole, // ✅ Pasamos el rol al HomeScreen
                onLogout = {
                    navController.navigate("login") {
                        popUpTo("home") { inclusive = true }
                    }
                }
            )
        }

        // Pantalla de configuración (solo para administradores)
        if (userRole == "Administrador") {
            composable("configuracion") {
                ConfiguracionScreen(
                    userRole = userRole,
                    navController = navController // Si necesitas navegación en la pantalla
                )
            }
        }
    }
}