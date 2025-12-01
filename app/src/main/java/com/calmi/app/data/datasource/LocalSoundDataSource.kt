package com.calmi.app.data.datasource

import com.calmi.app.data.model.SoundEntity

class LocalSoundDataSource {

    private val sounds = listOf(
        SoundEntity("ocean", "Ocean Waves", "file:///android_asset/images/ocean.jpg", "ocean.wav"),
        SoundEntity("rain", "Rain", "file:///android_asset/images/rain.jpg", "rain.wav"),
        SoundEntity("morning", "Morning", "file:///android_asset/images/morning.jpg", "morning.wav"),
        SoundEntity("fireplace", "Fireplace", "file:///android_asset/images/fireplace.jpg", "fireplace.wav"),
        SoundEntity("thunder_rain", "Thunder Rain", "file:///android_asset/images/thunder_rain.jpg", "thunder_rain.wav"),
        SoundEntity("water_flow", "Water Flow", "file:///android_asset/images/water_flow.jpg", "water_flow.wav"),
    )

    fun getDefaultSounds() = sounds
}
