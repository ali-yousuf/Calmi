package com.calmi.app.data.repository


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
}


private fun SoundEntity.toDomain() = Sound(
    id, name, "file:///android_asset/images/${image}", "file:///android_asset/sounds/${audio}"
)
