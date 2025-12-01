package com.calmi.app.domain.usecases

import com.calmi.app.domain.repository.SoundRepository


class GetAllSoundsUseCase(private val repository: SoundRepository) {
    operator fun invoke() = repository.getSounds()
}
