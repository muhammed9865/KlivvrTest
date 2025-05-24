package com.salman.klivvrandroidchallenge.data.di

import com.salman.klivvrandroidchallenge.data.repository.CityRepositoryImpl
import com.salman.klivvrandroidchallenge.data.search.CitySearchEngine
import com.salman.klivvrandroidchallenge.data.source.CityLocalSource
import com.salman.klivvrandroidchallenge.data.source.CountryImageSource
import com.salman.klivvrandroidchallenge.domain.SearchEngine
import com.salman.klivvrandroidchallenge.domain.model.CityItem
import com.salman.klivvrandroidchallenge.domain.repository.CityRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Created by Muhammed Salman email(mahmadslman@gmail.com) on 5/23/2025.
 */
@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @CitySearchEngineQualifier
    @Provides
    fun provideCitySearchEngine(): SearchEngine<CityItem> {
        return CitySearchEngine()
    }


    @Provides
    @Singleton
    fun provideCityRepository(
        @CitySearchEngineQualifier searchEngine: SearchEngine<CityItem>,
        cityLocalSource: CityLocalSource,
        countryImageSource: CountryImageSource
    ): CityRepository {
        return CityRepositoryImpl(cityLocalSource, searchEngine, countryImageSource)
    }

}