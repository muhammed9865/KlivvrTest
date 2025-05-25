package com.salman.klivvrandroidchallenge.presentation.screen

import androidx.compose.runtime.Stable
import com.salman.klivvrandroidchallenge.domain.model.LoadState
import com.salman.klivvrandroidchallenge.domain.model.GroupOfCity

/**
 * Created by Muhammed Salman email(mahmadslman@gmail.com) on 5/24/2025.
 */
@Stable
data class HomeState(
    val searchQuery: String? = null,
    val groupOfCities: LoadState<List<GroupOfCity>> = LoadState.Loading,
    val citiesCount: LoadState<Int> = LoadState.Loading,
    val isSearchBarExpanded: Boolean = false
)
