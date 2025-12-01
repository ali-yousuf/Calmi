package com.calmi.app.domain.model

data class Sound(
    val id: String,
    val name: String,
    val imagePath: String,
    val audio: String,
    val isFavorite: Boolean = false,
    val volume: Float = 1f,
    val isPlaying: Boolean = false
)
