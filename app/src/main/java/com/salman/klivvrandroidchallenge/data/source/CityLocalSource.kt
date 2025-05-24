package com.salman.klivvrandroidchallenge.data.source

import android.content.Context
import com.salman.klivvrandroidchallenge.R
import com.salman.klivvrandroidchallenge.data.model.CityEntity
import com.salman.klivvrandroidchallenge.domain.model.LoadState
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import javax.inject.Inject

/**
 * Created by Muhammed Salman email(mahmadslman@gmail.com) on 5/23/2025.
 */
class CityLocalSource @Inject constructor(
    @ApplicationContext private val context: Context,
    private val serializer: Json
) {

    companion object {
        private const val CITIES_FILE_NAME = "cities.json"
    }

    suspend fun getCities(): LoadState<List<CityEntity>> {
        return withContext(Dispatchers.IO) {
            try {
                val citiesJson =  context.assets.open(CITIES_FILE_NAME)
                    .bufferedReader()
                    .use { it.readText() }

                val cities = serializer.decodeFromString<List<CityEntity>>(citiesJson)
                LoadState.Success(cities)
            } catch (ex: Exception) {
                LoadState.Error(context.getString(R.string.something_went_wrong), ex)
            }
        }
    }

}