package com.salman.klivvrandroidchallenge.domain.model

import androidx.compose.runtime.Stable

/**
 * Created by Muhammed Salman email(mahmadslman@gmail.com) on 5/24/2025.
 */
@Stable
data class GroupOfCity(
    val startsByCharacter: Char,
    val cities: List<CityItem>
)