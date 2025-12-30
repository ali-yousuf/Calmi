package com.calmi.app.data.datasource

import com.calmi.app.data.model.SoundEntity

class LocalSoundDataSource {

    private val sounds = listOf(
        SoundEntity("ocean", "Ocean Waves", "ocean.jpg", "ocean.wav"),
        SoundEntity("rain", "Rain", "rain.jpg", "rain.wav"),
        SoundEntity("morning", "Morning", "morning.jpg", "morning.wav"),
        SoundEntity("fireplace", "Fireplace", "fireplace.jpg", "fireplace.wav"),
        SoundEntity("thunder_rain", "Thunder Rain", "thunder_rain.jpg", "thunder_rain.wav"),
        SoundEntity("water_flow", "Water Flow", "water_flow.jpg", "water_flow.wav"),
    )

    fun getDefaultSounds() = sounds
}
