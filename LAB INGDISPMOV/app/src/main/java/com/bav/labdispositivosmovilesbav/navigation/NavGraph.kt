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
import com.bav.labdispositivosmovilesbav.screens.ChatScreen
import com.bav.labdispositivosmovilesbav.screens.ConfiguracionScreen
import com.bav.labdispositivosmovilesbav.screens.HomeScreen
import com.bav.labdispositivosmovilesbav.screens.LoginScreen
import com.bav.labdispositivosmovilesbav.screens.RegisterScreen
import com.bav.labdispositivosmovilesbav.screens.SplashScreen
import com.bav.labdispositivosmovilesbav.screens.UserListScreen
import com.bav.labdispositivosmovilesbav.screens.VideoCallScreen
import com.bav.labdispositivosmovilesbav.viewmodels.ChatViewModel
import com.google.firebase.auth.FirebaseAuth
import io.agora.rtc.RtcEngine

@Composable
fun NavGraph(
    navController: NavHostController,
    userRole: String,
    rtcEngine: RtcEngine // Recibimos el RtcEngine desde MainActivity
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
                    navController.navigate("home") {
                        popUpTo(0) { inclusive = true }
                    }
                }
            }
        }

        // Ruta para la pantalla de video llamada, solo si el rol es adecuado
        composable("video_call/{channelName}") { backStackEntry ->
            val channelName = backStackEntry.arguments?.getString("channelName") ?: ""
            Log.d("NavGraph", "Canal de videollamada: $channelName")

            if (userRole == "Administrador" || userRole == "Usuario") {
                // Aquí pasamos el RtcEngine a la pantalla de videollamada
                VideoCallScreen(
                    rtcEngine = rtcEngine,  // Ahora se pasa la instancia de RtcEngine
                    channelName = channelName
                )
            } else {
                // Si no tiene el rol adecuado, redirigir a home
                LaunchedEffect(Unit) {
                    navController.navigate("home") {
                        popUpTo(0) { inclusive = true }
                    }
                }
            }
        }
    }
}
