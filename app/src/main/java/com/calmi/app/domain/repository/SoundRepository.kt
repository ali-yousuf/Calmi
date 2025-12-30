package com.calmi.app.domain.repository

import com.calmi.app.domain.model.Sound
import kotlinx.coroutines.flow.Flow

interface SoundRepository {

    fun getSounds(): Flow<List<Sound>>
}
