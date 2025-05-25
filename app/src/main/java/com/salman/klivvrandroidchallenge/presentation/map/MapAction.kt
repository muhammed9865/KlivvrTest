package com.salman.klivvrandroidchallenge.presentation.map

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.salman.klivvrandroidchallenge.R

/**
 * Created by Muhammed Salman email(mahmadslman@gmail.com) on 5/25/2025.
 */
enum class MapAction(
    @StringRes val titleResId: Int,
    @DrawableRes val iconResId: Int,
) {
    OPEN_MAP(
        titleResId = R.string.open_map,
        iconResId = R.drawable.map_icon
    ),
    NAVIGATE_TO_COORDINATES(
        titleResId = R.string.navigate_to_coordinates,
        iconResId = R.drawable.car_icon
    )
}