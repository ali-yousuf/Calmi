package com.calmi.app.domain.model

data class Sound(
    val id: String,
    val name: String,
    val imagePath: String,
    val audio: String,
    val isPlaying: Boolean = false
)
