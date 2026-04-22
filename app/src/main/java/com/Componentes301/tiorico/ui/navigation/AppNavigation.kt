package com.Componentes301.tiorico.ui.navigation

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.Componentes301.tiorico.ui.screens.GameScreen
import com.Componentes301.tiorico.ui.screens.WelcomeScreen
import com.Componentes301.tiorico.ui.viewmodel.GameViewModel

sealed class AppScreens(val route: String) {
    object Welcome : AppScreens("welcome_screen")
    object Game : AppScreens("game_screen")
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val context = LocalContext.current

    // ── READ DATA FROM THE INTENT ──
    val activity = context as? Activity
    val codigoSala = activity?.intent?.getStringExtra("codigoSala") ?: ""
    val nombreJugador = activity?.intent?.getStringExtra("nombreJugador") ?: ""

    // Determine where to go first
    // If codigoSala is not empty, it means we come from the waiting room
    val rutaInicial = if (codigoSala.isNotEmpty()) AppScreens.Game.route else AppScreens.Welcome.route

    NavHost(
        navController = navController,
        startDestination = rutaInicial // ← We use the calculated route
    ) {
        composable(route = AppScreens.Welcome.route) {
            WelcomeScreen(
                onStartLocalGame = { navController.navigate(AppScreens.Game.route) }
            )
        }

        composable(route = AppScreens.Game.route) {
            val viewModel: GameViewModel = viewModel()

            LaunchedEffect(Unit) {
                if (codigoSala.isNotEmpty()) {
                    viewModel.conectarAFirebase(codigoSala, nombreJugador)
                }
            }

            GameScreen(
                viewModel = viewModel,
                onExit = {
                    // If we exit an online match, it is better to close the activity
                    if (codigoSala.isNotEmpty()) {
                        activity?.finish()
                    } else {
                        navController.popBackStack()
                    }
                }
            )
        }
    }
}