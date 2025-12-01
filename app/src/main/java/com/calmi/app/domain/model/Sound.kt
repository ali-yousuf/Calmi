package com.calmi.app.domain.model

import android.net.Uri

data class Sound(
    val id: String,
    val name: String,
    val imagePath: String,
    val audioUri: Uri,
    val isFavorite: Boolean = false,
    val volume: Float = 1f
)
