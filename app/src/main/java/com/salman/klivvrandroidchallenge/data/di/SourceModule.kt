package com.salman.klivvrandroidchallenge.data.di

import android.content.Context
import com.salman.klivvrandroidchallenge.data.source.CityLocalSource
import com.salman.klivvrandroidchallenge.data.source.CountryImageSource
import com.salman.klivvrandroidchallenge.data.source.impl.CountryImageSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import javax.inject.Singleton

/**
 * Created by Muhammed Salman email(mahmadslman@gmail.com) on 5/24/2025.
 */

@Module
@InstallIn(SingletonComponent::class)
class SourceModule {

    @Provides
    @Singleton
    fun provideAppSerializer(): Json {
        return Json {
            ignoreUnknownKeys = false
            isLenient = true
            prettyPrint = true
        }
    }

    @Provides
    @Singleton
    fun provideCityLocalSource(
        @ApplicationContext context: Context,
        serializer: Json
    ): CityLocalSource {
        return CityLocalSource(
            context,
            serializer
        )
    }

    @Provides
    @Singleton
    fun provideCountryImageSource(
        @ApplicationContext context: Context
    ): CountryImageSource {
        return CountryImageSourceImpl(context)
    }
}