package com.calmi.app.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.calmi.app.di.SoundUseCases
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
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState

    init {
        loadSounds()
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
