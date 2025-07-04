package com.salman.klivvrandroidchallenge.presentation

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.salman.klivvrandroidchallenge.R
import com.salman.klivvrandroidchallenge.domain.model.CityItem
import com.salman.klivvrandroidchallenge.presentation.map.MapAction
import com.salman.klivvrandroidchallenge.presentation.map.MapsIntent
import com.salman.klivvrandroidchallenge.presentation.screen.HomeScreen
import com.salman.klivvrandroidchallenge.presentation.theme.KlivvrAndroidChallengeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<MainViewModel>()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val isDarkMode = viewModel.isDarkMode.collectAsStateWithLifecycle()
            updateSystemBarsAppearance(isNightMode = isDarkMode.value)
            KlivvrAndroidChallengeTheme(
                darkTheme = isDarkMode.value
            ) {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    HomeScreen(
                        modifier = Modifier
                            .fillMaxSize(),
                        isNightMode = isDarkMode.value,
                        onToggleNightMode = viewModel::toggleDarkMode
                    ) { cityItem, mapAction ->
                        openLocationInGoogleMaps(cityItem, mapAction)
                    }
                }
            }
        }
    }

    private fun updateSystemBarsAppearance(isNightMode: Boolean) {
        val insetsController = WindowInsetsControllerCompat(window, window.decorView)
        insetsController.isAppearanceLightNavigationBars = !isNightMode
        insetsController.isAppearanceLightStatusBars = !isNightMode
    }


    private fun openLocationInGoogleMaps(
        cityItem: CityItem,
        mapAction: MapAction = MapAction.OPEN_MAP
    ) {
        val intent = MapsIntent.builder(cityItem.coordinates)
            .zoom(20)
            .label(cityItem.name)
            .action(mapAction)
            .build()
            .toIntent()

        runCatching {
            intent.setPackage("com.google.android.apps.maps")
            intent.resolveActivity(packageManager).let {
                startActivity(intent)
            }
        }.onFailure {
            Toast.makeText(this, R.string.error_no_maps_app, Toast.LENGTH_SHORT).show()
        }
    }
}