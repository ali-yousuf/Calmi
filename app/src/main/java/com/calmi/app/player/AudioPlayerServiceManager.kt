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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.time.Duration


/**
 * Defines the contract for managing audio playback and timers.
 * This abstraction allows for decoupling the ViewModel from the concrete implementation.
 */
interface AudioPlayerManager {

    /** A flow that emits the current playback state of all sounds. (Map of soundId to isPlaying) */
    val isPlayingState: StateFlow<Map<String, Boolean>>

    /** A flow that emits the remaining time of the countdown timer in seconds. */
    val remainingTime: StateFlow<Long>

    /** Plays a given sound. */
    fun play(sound: Sound)

    /** Pauses a given sound. */
    fun pause(sound: Sound)

    /** Pauses all currently playing sounds and the timer. */
    fun pauseAll()

    /** Resumes playback for a given list of sounds and the timer. */
    fun resumeAll(sounds: List<Sound>)

    /** Sets the countdown timer to a specific duration. */
    fun setTimer(duration: Duration)

    /** Checks if a specific sound is currently marked as playing. */
    fun isPlaying(sound: Sound): Boolean

    /** Releases all resources used by the manager (service connection, timers, etc.). */
    fun release()
}


@Singleton
@RequiresApi(Build.VERSION_CODES.O)
class AudioPlayerServiceManager @Inject constructor(
    @param:ApplicationContext private val context: Context
) : AudioPlayerManager {
    private var audioPlayerService: AudioPlayerService? = null
    private val _isPlayingState = MutableStateFlow(mapOf<String, Boolean>())
    override val isPlayingState: StateFlow<Map<String, Boolean>> = _isPlayingState.asStateFlow()

    private val timer = PausableTimer()
    override val remainingTime: StateFlow<Long> = timer.remainingTime

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

    override fun play(sound: Sound) {
        audioPlayerService?.play(sound)
        _isPlayingState.update { it.toMutableMap().apply { put(sound.id, true) } }
        startTimer()
    }

    override fun pause(sound: Sound) {
        audioPlayerService?.pause(sound)
        _isPlayingState.update { it.toMutableMap().apply { put(sound.id, false) } }

        if (_isPlayingState.value.values.none { it }) {
            timer.reset()
        }
    }

    override fun isPlaying(sound: Sound): Boolean {
        return _isPlayingState.value[sound.id] ?: false
    }

    override fun pauseAll() {
        audioPlayerService?.pauseAll()
        _isPlayingState.update { currentMap ->
            currentMap.toMutableMap().apply {
                keys.forEach { key -> put(key, false) }
            }
        }
        timer.pause()
    }

    override fun resumeAll(sounds: List<Sound>) {
        audioPlayerService?.resumeAll()
        _isPlayingState.update { currentMap ->
            currentMap.toMutableMap().apply {
                sounds.forEach { sound ->
                    put(sound.id, true)
                }
            }
        }
        startTimer()
    }

    override fun setTimer(duration: Duration) {
        timer.setDuration(duration)
        if (_isPlayingState.value.values.any { it }) {
            startTimer()
        }
    }

    private fun startTimer() {
        timer.start {
            CoroutineScope(Dispatchers.Main).launch {
                pauseAll()
            }
        }
    }

    override fun release() {
        timer.release()
        Intent(context, AudioPlayerService::class.java).also { intent ->
            if (audioPlayerService != null) {
                context.unbindService(serviceConnection)
            }
            context.stopService(intent)
        }
    }
}

