package com.calmi.app.data.datasource

import com.calmi.app.data.model.SoundEntity

class LocalSoundDataSource {

    private val sounds = listOf(
        SoundEntity("ocean", "Ocean Waves", "file:///android_asset/images/ocean.jpg", "assets/sounds/ocean.wav"),
        SoundEntity("rain", "Rain", "file:///android_asset/images/rain.jpg", "assets/sounds/rain.wav"),
        SoundEntity("morning", "Morning", "file:///android_asset/images/morning.jpg", "assets/sounds/morning.wav"),
        SoundEntity("fireplace", "Fireplace", "file:///android_asset/images/fireplace.jpg", "assets/sounds/fireplace.wav"),
        SoundEntity("thunder_rain", "Thunder Rain", "file:///android_asset/images/thunder_rain.jpg", "assets/sounds/thunder_rain.wav"),
        SoundEntity("water_flow", "Water Flow", "file:///android_asset/images/water_flow.jpg", "assets/sounds/water_flow.wav"),
    )

    fun getDefaultSounds() = sounds
}
