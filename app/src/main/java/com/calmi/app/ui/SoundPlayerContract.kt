package com.calmi.app.ui

import com.calmi.app.domain.model.Sound
import com.calmi.app.utils.Constants
import com.calmi.app.utils.toMmSs
import kotlin.time.Duration

sealed interface SoundPlayerEvent {
    data object LoadSounds : SoundPlayerEvent
    data class SoundClicked(val sound: Sound) : SoundPlayerEvent
    data object PlayPauseClicked : SoundPlayerEvent
    data class PlaybackStateChanged(val playingStateMap: Map<String, Boolean>) : SoundPlayerEvent
    data class SoundsLoaded(val sounds: List<Sound>) : SoundPlayerEvent
    data class LoadFailed(val error: Throwable) : SoundPlayerEvent
    data class SetTimer(val duration: Duration) : SoundPlayerEvent
    data class TimerTicked(val remainingSeconds: Long) : SoundPlayerEvent
}

data class SoundPlayerUiState(
    val isLoading: Boolean = false,
    val isPlaying: Boolean = false,
    val soundList: List<Sound> = emptyList(),
    val activeSounds: List<Sound> = emptyList(),
    val errorMessage: String? = null,
    val timerDuration: Duration = Constants.DEFAULT_TIMER_DURATION,
    val remainingTime: Long = 0L
) {
    val hasActiveSounds: Boolean = activeSounds.isNotEmpty()


    /**
     * Provides the correctly formatted time string.
     * It shows the remaining countdown time if the timer is active,
     * otherwise it shows the total selected duration.
     */
    val formattedTime: String
        get() = if (remainingTime == 0L) {
            "OFF"
        } else if (remainingTime > 0) {
            remainingTime.toMmSs()
        } else {
            timerDuration.inWholeSeconds.toMmSs()
        }
}