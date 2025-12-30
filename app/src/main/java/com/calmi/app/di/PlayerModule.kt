package com.calmi.app.di

import com.calmi.app.player.AudioPlayerManager
import com.calmi.app.player.AudioPlayerServiceManager
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class PlayerModule {

    @Binds
    @Singleton
    abstract fun bindAudioPlayerManager(
        audioPlayerServiceManager: AudioPlayerServiceManager
        ): AudioPlayerManager

}
