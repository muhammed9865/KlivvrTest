package com.salman.klivvrandroidchallenge.data.mapper

import com.salman.klivvrandroidchallenge.data.model.CityEntity
import com.salman.klivvrandroidchallenge.domain.model.CityItem
import com.salman.klivvrandroidchallenge.domain.model.Coordinates
import com.salman.klivvrandroidchallenge.domain.model.ImageResource

/**
 * Created by Muhammed Salman email(mahmadslman@gmail.com) on 5/24/2025.
 */


fun CityEntity.toDomain(
    countryImageResource: ImageResource,
): CityItem {
    return CityItem(
        id = id,
        name = name,
        country = country,
        image = countryImageResource,
        coordinates = Coordinates(coordinates.lat, coordinates.lon)
    )
}