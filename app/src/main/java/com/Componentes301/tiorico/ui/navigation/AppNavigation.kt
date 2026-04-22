package com.Componentes301.tiorico.ui.navigation

import androidx.compose.runtime.Composable
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

    NavHost(
        navController = navController,
        startDestination = AppScreens.Welcome.route
    ) {
        composable(route = AppScreens.Welcome.route) {
            WelcomeScreen(
                onStartLocalGame = {
                    navController.navigate(AppScreens.Game.route)
                }
            )
        }

        composable(route = AppScreens.Game.route) {
            val viewModel: GameViewModel = viewModel()
            GameScreen(
                viewModel = viewModel,
                onSalir = { navController.popBackStack() }
            )
        }
    }
}