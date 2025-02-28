package com.bav.labdispositivosmovilesbav

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun NavGraph(startDestination: String = "splash") {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // Pantalla de carga inicial
        composable("splash") {
            SplashScreen(navController)  // Pantalla de carga inicial
        }

        // Pantalla de inicio/login
        composable("login") {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate("home") { // Ir a la pantalla principal o inicio
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
                onLogout = {
                    navController.navigate("login") {
                        popUpTo("home") { inclusive = true }
                    }
                }
            )
        }
    }
}
