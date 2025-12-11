package com.calmi.app.ui.screens.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.calmi.app.di.SoundUseCases
import com.calmi.app.domain.model.Sound
import com.calmi.app.player.AudioPlayerServiceManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
@RequiresApi(Build.VERSION_CODES.O)
class HomeViewModel @Inject constructor(
    private val soundsUseCase: SoundUseCases,
    private val audioPlayerServiceManager: AudioPlayerServiceManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())

    val uiState: StateFlow<HomeUiState> = combine(
        _uiState,
        audioPlayerServiceManager.isPlayingState
    ) { currentUiState, playingStateMap ->
        currentUiState.copy(
            soundList = currentUiState.soundList.map { s ->
                s.copy(isPlaying = playingStateMap[s.id] ?: false)
            }
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        HomeUiState()
    )

    init {
        loadSounds()
    }

    fun onSoundClicked(sound: Sound) {
        if (audioPlayerServiceManager.isPlaying(sound)) {
            audioPlayerServiceManager.pause(sound)
        } else {
            audioPlayerServiceManager.play(sound)
        }
    }

    fun onPlayPauseClicked() {
        if (uiState.value.soundList.any { it.isPlaying }) {
            audioPlayerServiceManager.pauseAll()
        } else {
            audioPlayerServiceManager.resumeAll()
        }
    }

    private fun loadSounds() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            soundsUseCase.getSounds().catch { error ->
                _uiState.update { currentState ->
                    currentState.copy(
                        isLoading = false,
                        errorMessage = error.message
                    )
                }
            }.collect { sounds ->
                _uiState.update { currentState ->
                    currentState.copy(
                        isLoading = false,
                        soundList = sounds.map { s ->
                            s.copy(isPlaying = audioPlayerServiceManager.isPlaying(s))
                        }
                    )
                }
            }
        }
    }
}

