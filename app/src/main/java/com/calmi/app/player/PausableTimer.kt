package com.calmi.app.player

import com.calmi.app.utils.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration

/**
 * A self-contained, pausable countdown timer that operates on a background thread.
 *
 * It exposes its remaining time via a StateFlow and provides simple methods
 * to control its state.
 */
class PausableTimer {

    private val scope = CoroutineScope(Dispatchers.Default)
    private var timerJob: Job? = null

    // The total duration for the timer, which can be set.
    private var _totalDuration: Duration = Constants.DEFAULT_TIMER_DURATION

    // The current remaining time in seconds, exposed publicly.
    private val _remainingTime = MutableStateFlow(0L)
    val remainingTime: StateFlow<Long> = _remainingTime.asStateFlow()

    /**
     * Sets a new duration for the timer. This stops any active countdown
     * and resets the remaining time to the new duration.
     */
    fun setDuration(duration: Duration) {
        timerJob?.cancel()
        _totalDuration = if (duration.isNegative()) Duration.ZERO else duration
        _remainingTime.update { _totalDuration.inWholeSeconds }
    }

    /**
     * Starts or resumes the timer if it's not already running and has time remaining.
     * If the timer was at zero, it resets to the total duration before starting.
     */
    fun start(onFinish: () -> Unit) {
        // Don't start a new timer if one is already active.
        if (timerJob?.isActive == true) return

        // If the timer finished, reset it to its total duration before starting again.
        if (_remainingTime.value <= 0L) {
            _remainingTime.update { _totalDuration.inWholeSeconds }
        }

        // Do not start if there's no time to count down.
        if (_remainingTime.value <= 0L) return

        timerJob = scope.launch {
            while (_remainingTime.value > 0) {
                delay(1000)
                _remainingTime.update { it - 1 }
            }
            // Once the loop finishes, execute the provided onFinish callback.
            onFinish()
        }
    }

    /**
     * Pauses the current countdown. The remaining time is preserved.
     */
    fun pause() {
        timerJob?.cancel()
    }

    /**
     * Resets the timer to its total configured duration and stops the countdown.
     */
    fun reset() {
        timerJob?.cancel()
        _remainingTime.update { _totalDuration.inWholeSeconds }
    }

    /**
     * Stops the timer and releases its resources.
     */
    fun release() {
        timerJob?.cancel()
    }
}
