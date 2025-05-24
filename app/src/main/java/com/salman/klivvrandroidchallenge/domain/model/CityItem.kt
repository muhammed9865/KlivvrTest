package com.salman.klivvrandroidchallenge.domain.model

/**
 * Created by Muhammed Salman email(mahmadslman@gmail.com) on 5/23/2025.
 *
 * Domain model represents a City item.
 *
 * Notes
 * - [searchKey] is used to sort and search the list of cities.
 */
data class CityItem(
    val id: Int,
    val name: String,
    val country: String,
    val image: ImageResource,
    val coordinates: Coordinates,
) {
    val searchKey: String
        get() = "${name.lowercase()}, ${country.lowercase()}"
}
