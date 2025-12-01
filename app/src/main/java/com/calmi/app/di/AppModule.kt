package com.calmi.app.di

import com.calmi.app.data.datasource.LocalSoundDataSource
import com.calmi.app.data.repository.SoundRepositoryImpl
import com.calmi.app.domain.repository.SoundRepository
import com.calmi.app.domain.usecases.GetAllSoundsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideLocalDataSource() = LocalSoundDataSource()

    @Provides
    @Singleton
    fun provideSoundRepository(
        dataSource: LocalSoundDataSource
    ): SoundRepository = SoundRepositoryImpl(dataSource)

    @Provides
    @Singleton
    fun provideUseCases(repository: SoundRepository) = SoundUseCases(
        getSounds = GetAllSoundsUseCase(repository),
    )
}

data class SoundUseCases(
    val getSounds: GetAllSoundsUseCase,
)
