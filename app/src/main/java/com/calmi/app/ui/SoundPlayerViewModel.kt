package com.calmi.app.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.calmi.app.di.SoundUseCases
import com.calmi.app.domain.model.Sound
import com.calmi.app.player.AudioPlayerServiceManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
@RequiresApi(Build.VERSION_CODES.O)
class SoundPlayerViewModel @Inject constructor(
    private val soundsUseCase: SoundUseCases,
    private val audioPlayerServiceManager: AudioPlayerServiceManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(SoundPlayerUiState())
    val uiState = _uiState.asStateFlow()

    init {
        audioPlayerServiceManager.isPlayingState
            .onEach { playingStateMap ->
                onEvent(SoundPlayerEvent.PlaybackStateChanged(playingStateMap))
            }
            .launchIn(viewModelScope)

        // Listen for timer countdown ticks
        audioPlayerServiceManager.remainingTime
            .onEach { remainingSeconds ->
                // Feed the time update back into the ViewModel
                onEvent(SoundPlayerEvent.TimerTicked(remainingSeconds))
            }
            .launchIn(viewModelScope)
        onEvent(SoundPlayerEvent.LoadSounds)
    }

    fun onEvent(event: SoundPlayerEvent) {
        when (event) {
            is SoundPlayerEvent.LoadSounds -> {
                viewModelScope.launch {
                    _uiState.update { it.copy(isLoading = true) }
                    soundsUseCase.getSounds()
                        .catch { error -> onEvent(SoundPlayerEvent.LoadFailed(error)) }
                        .collect { sounds -> onEvent(SoundPlayerEvent.SoundsLoaded(sounds)) }
                }
            }

            is SoundPlayerEvent.SoundsLoaded -> {
                _uiState.update { currentState ->
                    currentState.copy(
                        isLoading = false,
                        soundList = event.sounds.map { s ->
                            s.copy(isPlaying = audioPlayerServiceManager.isPlaying(s))
                        }
                    )
                }
            }

            is SoundPlayerEvent.LoadFailed -> {
                _uiState.update { it.copy(isLoading = false, errorMessage = event.error.message) }
            }

            is SoundPlayerEvent.SoundClicked -> {
                handleSoundClick(event.sound)
            }

            is SoundPlayerEvent.PlayPauseClicked -> {
                if (_uiState.value.isPlaying) {
                    audioPlayerServiceManager.pauseAll()
                } else {
                    val soundsToResume = _uiState.value.activeSounds
                    audioPlayerServiceManager.resumeAll(soundsToResume)
                }
            }

            is SoundPlayerEvent.PlaybackStateChanged -> {
                _uiState.update { currentState ->
                    currentState.copy(
                        isPlaying = event.playingStateMap.values.any { it },
                        soundList = currentState.soundList.map { s ->
                            s.copy(isPlaying = event.playingStateMap[s.id] ?: false)
                        },
                        activeSounds = currentState.activeSounds.map { s ->
                            s.copy(isPlaying = event.playingStateMap[s.id] ?: false)
                        }
                    )
                }
            }
            is SoundPlayerEvent.SetTimer -> {
                audioPlayerServiceManager.setTimer(event.duration)
                _uiState.update { it.copy(timerDuration = event.duration) }
            }
            is SoundPlayerEvent.TimerTicked -> {
                _uiState.update { currentState ->
                    currentState.copy(remainingTime = event.remainingSeconds)
                }
            }
        }
    }

    private fun handleSoundClick(sound: Sound) {
        val currentState = _uiState.value
        val soundInMix = currentState.activeSounds.find { it.id == sound.id }

        if (soundInMix != null) {
            audioPlayerServiceManager.pause(sound)
            _uiState.update {
                it.copy(activeSounds = it.activeSounds.filterNot { s -> s.id == sound.id })
            }
        } else {
            audioPlayerServiceManager.play(sound)
            _uiState.update {
                it.copy(activeSounds = it.activeSounds + sound)
            }
        }
    }
}


