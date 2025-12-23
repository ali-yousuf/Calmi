package com.calmi.app.player

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.IBinder
import androidx.annotation.RequiresApi
import com.calmi.app.domain.model.Sound
import com.calmi.app.utils.Constants
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.time.Duration

@Singleton
@RequiresApi(Build.VERSION_CODES.O)
class AudioPlayerServiceManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private var audioPlayerService: AudioPlayerService? = null
    private val _isPlayingState = MutableStateFlow(mapOf<String, Boolean>())
    val isPlayingState: StateFlow<Map<String, Boolean>> = _isPlayingState.asStateFlow()

    private val scope = CoroutineScope(Dispatchers.Default)
    private var timerJob: Job? = null
    private var _timerDuration: Duration = Constants.DEFAULT_TIMER_DURATION
    private val _remainingTime = MutableStateFlow(_timerDuration.inWholeSeconds)
    val remainingTime: StateFlow<Long> = _remainingTime.asStateFlow()

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
        startPausableTimer()
    }

    fun pause(sound: Sound) {
        audioPlayerService?.pause(sound)
        _isPlayingState.update { it.toMutableMap().apply { put(sound.id, false) } }

        // If no other sounds are playing, pause the timer
        if (_isPlayingState.value.values.none { it }) {
            // Reset the timer
            _remainingTime.value = _timerDuration.inWholeSeconds
            pauseTimer()
        }
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
        pauseTimer()
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
        startPausableTimer()
    }

    fun setTimer(duration: Duration) {
        pauseTimer()
        if (duration.isNegative() || duration == Duration.ZERO) {
            _remainingTime.value = 0L
            return
        }
        _timerDuration = duration
        _remainingTime.value = duration.inWholeSeconds
        startPausableTimer()
    }

    private fun startPausableTimer() {
        //reset the timer if resume again
        if (_remainingTime.value == 0L){
            _remainingTime.value = _timerDuration.inWholeSeconds
        }

        if (timerJob?.isActive == true || _remainingTime.value <= 0) return

        timerJob = scope.launch {
            while (_remainingTime.value > 0) {
                delay(1000)
                _remainingTime.update { it - 1 }
            }
            // When timer finishes, pause everything
            if (_remainingTime.value <= 0) {
                withContext(Dispatchers.Main) {
                    pauseAll()
                }
            }
        }
    }

    private fun pauseTimer() {
        timerJob?.cancel()
    }


    fun release() {
        Intent(context, AudioPlayerService::class.java).also { intent ->
            context.unbindService(serviceConnection)
            context.stopService(intent)
        }
    }
}
    