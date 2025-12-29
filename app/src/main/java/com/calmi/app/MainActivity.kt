package com.calmi.app

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.calmi.app.ui.NavGraph
import com.calmi.app.ui.theme.CalmiTheme
import com.calmi.app.utils.rememberPermissionHandler
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CalmiTheme {
                HandleNotificationPermission()
                NavGraph()
            }
        }
    }

    @Composable
    private fun HandleNotificationPermission() {
        val permissionHandler = rememberPermissionHandler()

        LaunchedEffect(Unit) {
            if (!permissionHandler.isPermissionGranted()) {
                permissionHandler.launchPermissionRequest()
            }
        }
    }
}

