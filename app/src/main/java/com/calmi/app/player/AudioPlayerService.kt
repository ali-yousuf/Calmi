package com.calmi.app.player

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.annotation.RequiresApi
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.calmi.app.domain.model.Sound
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@AndroidEntryPoint
@RequiresApi(Build.VERSION_CODES.O)
class AudioPlayerService : Service() {

    @Inject
    @ApplicationContext
    lateinit var context: Context

    @Inject
    lateinit var notificationHelper: NotificationHelper

    private val binder = AudioPlayerServiceBinder()
    private val mediaPlayers = mutableMapOf<String, ExoPlayer>()
    private val playingBeforePauseAll = mutableSetOf<String>()

    private var isForegroundService = false

    companion object Companion {
        const val ACTION_PAUSE_ALL = "com.calmi.app.PAUSE_ALL"
        const val ACTION_RESUME_ALL = "com.calmi.app.RESUME_ALL"
    }

    inner class AudioPlayerServiceBinder : Binder() {
        fun getService(): AudioPlayerService = this@AudioPlayerService
    }

    override fun onBind(intent: Intent?): IBinder = binder

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_PAUSE_ALL -> pauseAll()
            ACTION_RESUME_ALL -> resumeAll()
        }
        return START_STICKY
    }

    fun play(sound: Sound) {
        if (!isForegroundService) {
            startForegroundService()
        }

        if (mediaPlayers.containsKey(sound.id)) {
            mediaPlayers[sound.id]?.play()
        } else {
            val player = ExoPlayer.Builder(context).build().apply {
                repeatMode = Player.REPEAT_MODE_ONE
                val mediaItem = MediaItem.fromUri(sound.audio)
                setMediaItem(mediaItem)
                prepare()
                play()
                addListener(object : Player.Listener {
                    override fun onIsPlayingChanged(isPlaying: Boolean) {
                        updateNotification()
                    }
                })
            }
            mediaPlayers[sound.id] = player
        }
        updateNotification()
    }

    fun pause(sound: Sound) {
        mediaPlayers[sound.id]?.pause()
        updateNotification()
    }

    fun isPlaying(sound: Sound): Boolean {
        return mediaPlayers[sound.id]?.isPlaying ?: false
    }

    fun pauseAll() {
        playingBeforePauseAll.clear()
        mediaPlayers.forEach { (id, player) ->
            if (player.isPlaying) {
                playingBeforePauseAll.add(id)
                player.pause()
            }
        }
        updateNotification()
        stopForegroundServiceIfNoPlayback()
    }

    fun resumeAll() {
        if (!isForegroundService) {
            startForegroundService()
        }
        playingBeforePauseAll.forEach { id ->
            mediaPlayers[id]?.play()
        }
        updateNotification()
    }

    private fun startForegroundService() {
        val notification = notificationHelper.buildNotification(isPlaying = true).build()
        startForeground(notificationHelper.getNotificationId(), notification)
        isForegroundService = true
    }

    private fun stopForegroundServiceIfNoPlayback() {
        if (mediaPlayers.values.none { it.isPlaying } && isForegroundService) {
            stopForeground(Service.STOP_FOREGROUND_REMOVE)
            isForegroundService = false
        }
    }

    private fun updateNotification() {
        val anyPlaying = mediaPlayers.values.any { it.isPlaying }
        val notification = notificationHelper.buildNotification(anyPlaying).build()
        notificationHelper.getNotificationManager().notify(notificationHelper.getNotificationId(), notification)
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayers.values.forEach { it.release() }
        mediaPlayers.clear()
        stopForeground(STOP_FOREGROUND_REMOVE)
    }
}
