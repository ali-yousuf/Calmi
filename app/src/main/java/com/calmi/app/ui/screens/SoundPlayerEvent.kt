package com.calmi.app.ui.screens

import com.calmi.app.domain.model.Sound

sealed interface SoundPlayerEvent {
    data object LoadSounds : SoundPlayerEvent
    data class SoundClicked(val sound: Sound) : SoundPlayerEvent
    data object PlayPauseClicked : SoundPlayerEvent
    data class PlaybackStateChanged(val playingStateMap: Map<String, Boolean>) : SoundPlayerEvent
    data class SoundsLoaded(val sounds: List<Sound>) : SoundPlayerEvent
    data class LoadFailed(val error: Throwable) : SoundPlayerEvent
}