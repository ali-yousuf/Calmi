package com.calmi.app.navigation

sealed class Screens(val route: String) {
    object Splash : Screens("splash_screen")
    object Home : Screens("home_screen")
    object Player : Screens("player_screen")
}