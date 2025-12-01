package com.calmi.app.data.model

data class SoundEntity(
    val id: String,
    val name: String,
    val imagePath: String,
    val audioPath: String,
    val isFavorite: Boolean = false,
    val volume: Float = 1f
)
