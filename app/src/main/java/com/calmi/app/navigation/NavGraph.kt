package com.calmi.app.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.calmi.app.ui.screens.home.HomeScreen
import com.calmi.app.ui.screens.player.PlayerScreen
import com.calmi.app.ui.screens.SplashScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavGraph() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Screens.Splash.route
    ) {
        composable(Screens.Splash.route) {
            SplashScreen(navController = navController)
        }
        composable(Screens.Home.route) {
            HomeScreen(navController = navController)
        }
        composable(
            Screens.Player.route,
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