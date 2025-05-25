package com.salman.klivvrandroidchallenge.domain.usecase

import com.salman.klivvrandroidchallenge.domain.model.CityItem
import com.salman.klivvrandroidchallenge.domain.model.GroupOfCity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by Muhammed Salman email(mahmadslman@gmail.com) on 5/24/2025.
 *
 * Groups a list of cities by the first character of their names.
 *
 * It uses [Dispatchers.Default] to perform the grouping operation in a background thread,
 * due to the potentially large size of the list.
 */
class GroupCitiesByCharacterUseCase @Inject constructor() {

    suspend operator fun invoke(
        cities: List<CityItem>,
    ): List<GroupOfCity> = withContext(Dispatchers.Default) {
        return@withContext cities
            .groupBy { it.name.first() }
            .map { (character, cities) ->
                GroupOfCity(
                    startsByCharacter = character,
                    cities = cities
                )
            }

    }

}