package com.salman.klivvrandroidchallenge.presentation.screen

import androidx.compose.runtime.Stable
import com.salman.klivvrandroidchallenge.domain.model.CityItem
import com.salman.klivvrandroidchallenge.domain.model.Coordinates
import com.salman.klivvrandroidchallenge.domain.model.LoadState
import com.salman.klivvrandroidchallenge.domain.model.GroupOfCity
import com.salman.klivvrandroidchallenge.domain.model.ImageResource

/**
 * Created by Muhammed Salman email(mahmadslman@gmail.com) on 5/24/2025.
 */
@Stable
data class HomeState(
    val searchQuery: String? = null,
    val groupOfCities: LoadState<List<GroupOfCity>> = LoadState.Loading,
    val citiesCount: LoadState<Int> = LoadState.Loading,
    val isSearchBarExpanded: Boolean = false,
    val showCityItemMapActions: CityItem? = null,
) {

    companion object {
        fun preview(expanded: Boolean = false): HomeState {
            val city1 = CityItem(
                id = 1,
                name = "Cairo",
                country = "Egypt",
                image = ImageResource.Res(resId = android.R.drawable.ic_menu_mylocation),
                coordinates = Coordinates(30.0444, 31.2357)
            )
            val city2 = CityItem(
                id = 2,
                name = "Alexandria",
                country = "Egypt",
                image = ImageResource.Res(resId = android.R.drawable.ic_menu_mylocation),
                coordinates = Coordinates(31.2001, 29.9187)
            )
            val group = GroupOfCity(
                startsByCharacter = 'C',
                cities = listOf(city1, city2)
            )
            return HomeState(
                searchQuery = "",
                groupOfCities = LoadState.Success(listOf(group)),
                citiesCount = LoadState.Success(2),
                isSearchBarExpanded = expanded,
                showCityItemMapActions = null
            )
        }
    }
}
