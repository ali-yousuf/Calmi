package com.calmi.app.data.repository


import androidx.core.net.toUri
import com.calmi.app.data.datasource.LocalSoundDataSource
import com.calmi.app.data.model.SoundEntity
import com.calmi.app.domain.model.Sound
import com.calmi.app.domain.repository.SoundRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SoundRepositoryImpl(
    localDataSource: LocalSoundDataSource
) : SoundRepository {

    private val soundsFlow = MutableStateFlow(localDataSource.getDefaultSounds())

    override fun getSounds(): Flow<List<Sound>> =
        soundsFlow.map { list ->
            list.map { it.toDomain() }
        }

    override suspend fun toggleFavorite(id: String) {
        soundsFlow.value = soundsFlow.value.map {
            if (it.id == id) it.copy(isFavorite = !it.isFavorite) else it
        }
    }

    override suspend fun updateVolume(id: String, volume: Float) {
        soundsFlow.value = soundsFlow.value.map {
            if (it.id == id) it.copy(volume = volume) else it
        }
    }
}

// Mapper
private fun SoundEntity.toDomain() = Sound(
    id, name, imagePath, audioPath.toUri(), isFavorite, volume
)
