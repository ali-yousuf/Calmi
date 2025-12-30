package com.calmi.app.player

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.calmi.app.MainActivity
import com.calmi.app.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

object NotificationConstants {
    const val PLAYBACK_CHANNEL_ID = "calmi_playback_channel"
    const val PLAYBACK_CHANNEL_NAME = "Calmi Playback"
    const val PLAYBACK_NOTIFICATION_ID = 1001
}


@RequiresApi(Build.VERSION_CODES.O)
class NotificationHelper @Inject constructor(
    @param:ApplicationContext private val context: Context
) {

    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    init {
        createNotificationChannel()
    }


    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            NotificationConstants.PLAYBACK_CHANNEL_ID,
            NotificationConstants.PLAYBACK_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_LOW
        ).apply {
            description = "Media playback controls and status for Calmi sounds."
            lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            setSound(null, null)
            enableVibration(false)
        }

        notificationManager.createNotificationChannel(channel)
    }


    fun buildNotification(isPlaying: Boolean): NotificationCompat.Builder {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val playPauseAction = if (isPlaying) {
            NotificationCompat.Action(
                R.drawable.ic_pause,
                "Pause",
                createPlaybackAction(context, AudioPlayerService.ACTION_PAUSE_ALL)
            )
        } else {
            NotificationCompat.Action(
                R.drawable.ic_play,
                "Play",
                createPlaybackAction(context, AudioPlayerService.ACTION_RESUME_ALL)
            )
        }

        return NotificationCompat.Builder(context, NotificationConstants.PLAYBACK_CHANNEL_ID)
            .setContentTitle("Calmi")
            .setContentText("Playing sounds in the background")
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .addAction(playPauseAction)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
    }

    private fun createPlaybackAction(context: Context, action: String): PendingIntent {
        val intent = Intent(context, AudioPlayerService::class.java).apply { this.action = action }
        return PendingIntent.getService(
            context,
            action.hashCode(),
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    fun getNotificationId(): Int = NotificationConstants.PLAYBACK_NOTIFICATION_ID


    fun getNotificationManager(): NotificationManager = notificationManager
}
