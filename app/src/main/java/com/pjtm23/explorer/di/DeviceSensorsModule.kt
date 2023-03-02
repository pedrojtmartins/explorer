package com.pjtm23.explorer.di

import android.content.Context
import android.content.Context.SENSOR_SERVICE
import android.hardware.Sensor
import android.hardware.SensorManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DeviceSensorsModule {

    @Provides
    @Singleton
    fun provideFusedLocationProviderClient(
            @ApplicationContext context: Context
    ): FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)

    @Provides
    @Singleton
    fun providesSensorManager(
            @ApplicationContext context: Context
    ): SensorManager = context.getSystemService(SENSOR_SERVICE) as SensorManager

    @Provides
    @Singleton
    @SensorAccelerometer
    fun provideAccelerometer(
            sensorManager: SensorManager
    ): Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

    @Provides
    @Singleton
    @SensorMagnetometer
    fun provideMagnetometer(
            sensorManager: SensorManager
    ): Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class SensorMagnetometer

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class SensorAccelerometer
