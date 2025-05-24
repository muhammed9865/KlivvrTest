package com.salman.klivvrandroidchallenge.data.repository

import com.salman.klivvrandroidchallenge.data.mapper.toDomain
import com.salman.klivvrandroidchallenge.data.source.CityLocalSource
import com.salman.klivvrandroidchallenge.data.source.CountryImageSource
import com.salman.klivvrandroidchallenge.domain.SearchEngine
import com.salman.klivvrandroidchallenge.domain.model.CityItem
import com.salman.klivvrandroidchallenge.domain.model.LoadState
import com.salman.klivvrandroidchallenge.domain.repository.CityRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

/**
 * Created by Muhammed Salman email(mahmadslman@gmail.com) on 5/23/2025.
 */
class CityRepositoryImpl @Inject constructor(
    private val cityLocalSource: CityLocalSource,
    private val searchEngine: SearchEngine<CityItem>,
    private val countryImageSource: CountryImageSource,
) : CityRepository {

    private val citiesState = MutableStateFlow<LoadState<List<CityItem>>>(LoadState.Idle)
    override val loadCityState: StateFlow<LoadState<List<CityItem>>>
        get() = citiesState

    override suspend fun loadCities(): LoadState<List<CityItem>> {
        // Skip loading if the cities are already loaded or loading
        if (citiesState.value.isLoading() or citiesState.value.isSuccess())
            return citiesState.value

        citiesState.update { LoadState.Loading }

        val citiesResult = cityLocalSource.getCities()
        if (citiesResult.isSuccess()) {
            val cities = citiesResult.dataOrNull()!!.map {
                it.toDomain(countryImageSource.getCountryImage(it.country))
            }
            val citiesSorted = cities.sortedBy { it.searchKey }
            citiesState.update { LoadState.Success(citiesSorted) }
        }

        if (citiesResult.isError()) {
            citiesResult.onError { _, ex -> ex?.printStackTrace()}
            citiesState.update { citiesResult as LoadState.Error }
        }

        return citiesState.value
    }

    override suspend fun searchCities(query: String): List<CityItem> {
        val cities = citiesState.value.dataOrNull() ?: return emptyList()
        return searchEngine.search(cities, query)
    }
}