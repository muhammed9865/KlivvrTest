package com.salman.klivvrandroidchallenge

import android.app.Application
import com.salman.klivvrandroidchallenge.domain.repository.CityRepository
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

/**
 * Created by Muhammed Salman email(mahmadslman@gmail.com) on 5/23/2025.
 */
@HiltAndroidApp
class KlivvrApp: Application(), CoroutineScope {

    @Inject lateinit var cityRepository: CityRepository

    override val coroutineContext: CoroutineContext
        get() = SupervisorJob()

    override fun onCreate() {
        super.onCreate()

        // Preload the cities data
        launch(Dispatchers.IO) {
            cityRepository.loadCities()
        }
    }

}