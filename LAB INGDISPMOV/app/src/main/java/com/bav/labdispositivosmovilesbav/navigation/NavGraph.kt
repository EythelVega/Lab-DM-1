package com.bav.labdispositivosmovilesbav.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.bav.labdispositivosmovilesbav.screens.CatalogScreen
import com.bav.labdispositivosmovilesbav.screens.FacebookScreen
import com.bav.labdispositivosmovilesbav.screens.HomeScreen
import com.bav.labdispositivosmovilesbav.screens.LoginScreen
import com.bav.labdispositivosmovilesbav.screens.ManageProductsScreen
import com.bav.labdispositivosmovilesbav.screens.RegisterScreen
import com.bav.labdispositivosmovilesbav.screens.SplashScreen
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
        
        composable("catalog") {
            CatalogScreen(
                navController = navController,
                userRole = userRole
            )
        }
        
        composable("manage_products") {
            ManageProductsScreen(
                navController = navController
            )
        }
        
        composable("facebook") {
            FacebookScreen(navController = navController)
        }
    }
}
