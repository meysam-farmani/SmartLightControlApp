package com.example.smartlight.di

import com.example.smartlight.data.mqtt.MqttManager
import com.example.smartlight.data.repository.LightRepositoryImpl
import com.example.smartlight.data.repository.LightRepository
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
    fun provideMqttManager(): MqttManager {
        return MqttManager()
    }

    @Provides
    @Singleton
    fun provideLightRepository(mqttManager: MqttManager): LightRepository {
        return LightRepositoryImpl(mqttManager)
    }
}
