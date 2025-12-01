package com.calmi.app.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.calmi.app.di.SoundUseCases
import com.calmi.app.domain.model.Sound
import com.calmi.app.player.AudioPlayer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val soundsUseCase: SoundUseCases,
    private val audioPlayer: AudioPlayer,
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState

    init {
        loadSounds()
    }

    fun onSoundClicked(sound: Sound) {
        if (audioPlayer.isPlaying(sound)) {
            audioPlayer.pause(sound)
        } else {
            audioPlayer.play(sound)
        }
        _uiState.update {
            it.copy(
                soundList = it.soundList.map { s->
                    if (s.id == sound.id) {
                        s.copy(isPlaying = audioPlayer.isPlaying(sound))
                    } else {
                        s
                    }
                }
            )
        }
    }

    fun onPlayPauseClicked() {
        val currentlyPlaying = uiState.value.soundList.any { it.isPlaying }
        if (currentlyPlaying) {
            audioPlayer.pauseAll()
        } else {
            audioPlayer.resumeAll()
        }
        _uiState.update {
            it.copy(
                soundList = it.soundList.map { s ->
                    s.copy(isPlaying = audioPlayer.isPlaying(s))
                }
            )
        }
    }

    override fun onCleared() {
        super.onCleared()
        audioPlayer.release()
    }

    private fun loadSounds() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            soundsUseCase.getSounds().catch {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = it.errorMessage
                    )
                }
            }.collect { sounds ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        soundList = sounds
                    )
                }
            }
        }
    }
}
