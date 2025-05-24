package com.salman.klivvrandroidchallenge.domain.repository

import com.salman.klivvrandroidchallenge.domain.model.CityItem
import com.salman.klivvrandroidchallenge.domain.model.LoadState
import kotlinx.coroutines.flow.StateFlow

/**
 * Created by Muhammed Salman email(mahmadslman@gmail.com) on 5/23/2025.
 */
interface CityRepository {

    /**
     * Represents the current loading state of the cities.
     */
    val loadCityState: StateFlow<LoadState<List<CityItem>>>

    /**
     * Loads the list of cities and updates the [loadCityState].
     *
     * It should store a reference to cities to avoid reloading them unnecessarily.
     */
    suspend fun loadCities(): LoadState<List<CityItem>>


    /**
     * Searches for cities based on the provided [query].
     * Efficient search that uses the [CityItem.searchKey] of each city.
     */
    suspend fun searchCities(query: String): List<CityItem>
}