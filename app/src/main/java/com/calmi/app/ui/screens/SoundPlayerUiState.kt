package com.calmi.app.ui.screens

import com.calmi.app.domain.model.Sound

data class SoundPlayerUiState(
    val isLoading: Boolean = false,
    val isPlaying: Boolean = false,
    val soundList: List<Sound> = emptyList(),
    val activeSounds: List<Sound> = emptyList(),
    val errorMessage: String? = null
) {
    val hasActiveSounds: Boolean = activeSounds.isNotEmpty()
}
