package com.calmi.app.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.calmi.app.ui.home.HomeScreen
import com.calmi.app.ui.player.PlayerScreen
import com.calmi.app.ui.splash.SplashScreen

sealed class Route(val route: String) {
    object Splash : Route("splash")
    object Home : Route("home")
    object Player : Route("player")
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavGraph() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Route.Splash.route
    ) {
        composable("splash") {
            SplashScreen {
                navController.navigate("home") {
                    popUpTo("splash") { inclusive = true }
                }
            }
        }
        composable(Route.Home.route) {
            HomeScreen(navController = navController)
        }
        composable(
            Route.Player.route,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Up,
                    animationSpec = tween(500)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Down,
                    animationSpec = tween(500)
                )
            }
        ) {
            PlayerScreen(navController = navController)
        }
    }
}