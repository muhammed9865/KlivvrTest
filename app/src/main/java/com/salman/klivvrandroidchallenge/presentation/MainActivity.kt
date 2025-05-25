package com.salman.klivvrandroidchallenge.presentation

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.salman.klivvrandroidchallenge.R
import com.salman.klivvrandroidchallenge.domain.model.CityItem
import com.salman.klivvrandroidchallenge.presentation.map.MapAction
import com.salman.klivvrandroidchallenge.presentation.map.MapsIntent
import com.salman.klivvrandroidchallenge.presentation.screen.HomeScreen
import com.salman.klivvrandroidchallenge.presentation.theme.KlivvrAndroidChallengeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KlivvrAndroidChallengeTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopAppBar(
                            title = { Text(text = stringResource(R.string.city_search)) },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = MaterialTheme.colorScheme.background
                            )
                        )
                    },
                ) { innerPadding ->
                    HomeScreen(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                    ) { cityItem, mapAction ->
                        openLocationInGoogleMaps(cityItem, mapAction)
                    }
                }
            }
        }
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