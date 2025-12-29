package com.calmi.app.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat

/**
 * A stateful utility class that handles Notification permission logic.
 *
 * This class is designed to be created within a Composable using the [rememberPermissionHandler] function.
 */
class NotificationPermissionHandler(
    private val context: Context,
    private val permissionLauncher: ManagedActivityResultLauncher<String, Boolean>
) {

    /**
     * Checks if the POST_NOTIFICATIONS permission has been granted.
     * @return True if granted, false otherwise.
     */
    fun isPermissionGranted(): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            return true
        }
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED
    }

    /**
     * Launches the system permission dialog to request the POST_NOTIFICATIONS permission.
     */
    fun launchPermissionRequest() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }
}

/**
 * A Composable function to remember a [NotificationPermissionHandler] across recompositions.
 *
 * This manages the lifecycle of the ActivityResultLauncher and provides a clean way
 * to interact with the permission system.
 */
@Composable
fun rememberPermissionHandler(): NotificationPermissionHandler {
    val context = LocalContext.current

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        val message = if (isGranted) {
            "Permission Granted"
        } else {
            "Permission Denied. Notifications will not be shown."
        }
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    return remember(context, permissionLauncher) {
        NotificationPermissionHandler(context, permissionLauncher)
    }
}
