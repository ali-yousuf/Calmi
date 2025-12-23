package com.calmi.app.ui.screens

import com.calmi.app.domain.model.Sound
import com.calmi.app.utils.Constants
import com.calmi.app.utils.toMmSs
import kotlin.time.Duration

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
