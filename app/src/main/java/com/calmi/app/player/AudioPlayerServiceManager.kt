package com.calmi.app.player

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.IBinder
import androidx.annotation.RequiresApi
import com.calmi.app.domain.model.Sound
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@RequiresApi(Build.VERSION_CODES.O)
class AudioPlayerServiceManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private var audioPlayerService: AudioPlayerService? = null
    private val _isPlayingState = MutableStateFlow(mapOf<String, Boolean>())
    val isPlayingState: StateFlow<Map<String, Boolean>> = _isPlayingState.asStateFlow()

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as AudioPlayerService.AudioPlayerServiceBinder
            audioPlayerService = binder.getService()
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            audioPlayerService = null
        }
    }

    init {
        Intent(context, AudioPlayerService::class.java).also { intent ->
            context.startService(intent)
            context.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
        }
    }

    fun play(sound: Sound) {
        audioPlayerService?.play(sound)
        _isPlayingState.update { it.toMutableMap().apply { put(sound.id, true) } }
    }

    fun pause(sound: Sound) {
        audioPlayerService?.pause(sound)
        _isPlayingState.update { it.toMutableMap().apply { put(sound.id, false) } }
    }

    fun isPlaying(sound: Sound): Boolean {
        return _isPlayingState.value[sound.id] ?: false
    }

    fun pauseAll() {
        audioPlayerService?.pauseAll()
        _isPlayingState.update { currentMap ->
            currentMap.toMutableMap().apply {
                keys.forEach { key -> put(key, false) }
            }
        }
    }

    fun resumeAll(sounds: List<Sound>) {
        audioPlayerService?.resumeAll()
        _isPlayingState.update { currentMap ->
            currentMap.toMutableMap().apply {
                sounds.forEach { sound ->
                    put(sound.id, true)
                }
            }
        }
    }

    fun release() {
        Intent(context, AudioPlayerService::class.java).also { intent ->
            context.unbindService(serviceConnection)
            context.stopService(intent)
        }
    }
}
    