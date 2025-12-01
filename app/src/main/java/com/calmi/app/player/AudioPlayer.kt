package com.calmi.app.player

import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.calmi.app.domain.model.Sound
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class AudioPlayer @Inject constructor(
    @ApplicationContext private val context: Context,
) {

    private val mediaPlayers = mutableMapOf<String, ExoPlayer>()

    fun play(sound: Sound) {
        if (mediaPlayers.containsKey(sound.id)) {
            mediaPlayers[sound.id]?.play()
        } else {
            val player = ExoPlayer.Builder(context).build()
            player.repeatMode = Player.REPEAT_MODE_ONE
            val mediaItem = MediaItem.fromUri(sound.audio)
            player.setMediaItem(mediaItem)
            player.prepare()
            player.play()
            mediaPlayers[sound.id] = player
        }
    }

    fun pause(sound: Sound) {
        mediaPlayers[sound.id]?.pause()
    }

    fun isPlaying(sound: Sound): Boolean {
        return mediaPlayers[sound.id]?.isPlaying ?: false
    }

    fun pauseAll() {
        mediaPlayers.values.forEach { it.pause() }
    }

    fun resumeAll() {
        mediaPlayers.values.forEach { it.play() }
    }

    fun release() {
        mediaPlayers.values.forEach { it.release() }
        mediaPlayers.clear()
    }
}
