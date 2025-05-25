package com.salman.klivvrandroidchallenge.presentation.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.salman.klivvrandroidchallenge.domain.model.CityItem
import com.salman.klivvrandroidchallenge.domain.model.LoadState
import com.salman.klivvrandroidchallenge.domain.repository.CityRepository
import com.salman.klivvrandroidchallenge.domain.usecase.GroupCitiesByCharacterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by Muhammed Salman email(mahmadslman@gmail.com) on 5/24/2025.
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val cityRepository: CityRepository,
    private val groupCitiesUseCase: GroupCitiesByCharacterUseCase,
) : ViewModel() {

    private val mutableState = MutableStateFlow(HomeState())
    val state = mutableState.asStateFlow()

    private val searchFlow = MutableStateFlow<String?>(null)

    init {
        observeCities()
        observeSearchQueries()
    }

    fun onSearchQueryChanged(query: String) {
        mutableState.update { it.copy(searchQuery = query.takeIf { it.isNotBlank() }) }
        if (state.value.groupOfCities.isLoading()) return
        searchFlow.update { query }
    }

    fun onSearchBarExpanded(isExpanded: Boolean) {
        mutableState.update { it.copy(isSearchBarExpanded = isExpanded) }
    }

    fun openMapActionsFor(cityItem: CityItem) {
        mutableState.update {
            it.copy(showCityItemMapActions = cityItem)
        }
    }

    fun hideMapActions() {
        mutableState.update {
            it.copy(showCityItemMapActions = null)
        }
    }


    private fun observeCities() = viewModelScope.launch {
        cityRepository.loadCityState
            .filter { it.isSuccess() }
            .collectLatest {
                val cities = it.dataOrNull().orEmpty()
                groupCities(cities)
            }
    }

    @OptIn(FlowPreview::class)
    private fun observeSearchQueries() = viewModelScope.launch {
        searchFlow
            .filterNotNull()
            .collectLatest {
                performSearch(query = it)
            }
    }

    private suspend fun performSearch(query: String) = withContext(Dispatchers.Default) {
        val cities = cityRepository.searchCities(query)
        groupCities(cities)
    }

    private suspend fun groupCities(cities: List<CityItem>) {
        val citiesCount = cities.size
        val groupedCities = groupCitiesUseCase(cities)
        mutableState.update {
            it.copy(
                groupOfCities = LoadState.Success(groupedCities),
                citiesCount = LoadState.Success(citiesCount)
            )
        }
    }
}