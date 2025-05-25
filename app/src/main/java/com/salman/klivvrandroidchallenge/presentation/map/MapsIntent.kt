package com.salman.klivvrandroidchallenge.presentation.map

import android.content.Intent
import android.net.Uri
import android.util.Log
import com.salman.klivvrandroidchallenge.domain.model.Coordinates
import androidx.core.net.toUri

/**
 * Created by Muhammed Salman email(mahmadslman@gmail.com) on 5/25/2025.
 *
 * Represents an intent to launch a maps application with configurable behavior.
 *
 * This class supports two main use cases:
 * 1. Viewing a location on the map with an optional label or search query.
 * 2. Launching navigation to a specific set of coordinates.
 *
 * The generated [Intent] uses either the `geo:` or `google.navigation:` URI scheme,
 * depending on the specified [MapAction].
 *
 * Usage is encouraged via the [Builder] pattern to ensure readability and immutability.
 *
 * Example:
 * ```
 * val intent = MapsIntent.builder(12.34, 56.78)
 *     .label("Home")
 *     .zoom(16)
 *     .action(MapAction.OPEN_MAP)
 *     .build()
 *     .toIntent()
 * ```
 *
 * @param latitude Latitude of the location.
 * @param longitude Longitude of the location.
 * @param label Optional label for display (used only with OPEN_MAP).
 * @param query Optional search query to override label.
 * @param zoom Optional zoom level (applies only to OPEN_MAP).
 * @param action Defines the intent behavior (open map or navigate).
*/
data class MapsIntent(
    private val latitude: Double,
    private val longitude: Double,
    private val label: String? = null,
    private val query: String? = null,
    private val zoom: Int = 15,
    private val action: MapAction = MapAction.OPEN_MAP
) {

    fun toIntent(): Intent {
        val uri = buildUri()
        Log.d("MapsIntent", "Generated URI: $uri")
        return Intent(Intent.ACTION_VIEW, uri)
    }

    private fun buildUri(): Uri {
        return when (action) {
            MapAction.OPEN_MAP -> buildGeoUri()
            MapAction.NAVIGATE_TO_COORDINATES -> buildNavigationUri()
        }
    }

    private fun buildGeoUri(): Uri {
        val base = "geo:$latitude,$longitude"

        val queryParam = when {
            query != null -> "q=${Uri.encode(query)}"
            label != null -> {
                val coordWithLabel = "$latitude,$longitude(${label})"
                "q=${Uri.encode(coordWithLabel)}"
            }
            else -> null
        }

        val params = buildList {
            queryParam?.let { add(it) }
            if (zoom > 0) add("z=$zoom")
        }

        val uriString = if (params.isNotEmpty()) {
            "$base?${params.joinToString("&")}"
        } else {
            base
        }

        return uriString.toUri()
    }

    private fun buildNavigationUri(): Uri {
        val uriString = "google.navigation:q=$latitude,$longitude"
        return uriString.toUri()
    }

    // ... Builder remains mostly unchanged, but we'll add the new option:
    companion object {
        class Builder(
            private var latitude: Double = 0.0,
            private var longitude: Double = 0.0,
        ) {
            private var label: String? = null
            private var query: String? = null
            private var zoom: Int = 15
            private var action: MapAction = MapAction.OPEN_MAP

            fun latitude(latitude: Double) = apply { this.latitude = latitude }
            fun longitude(longitude: Double) = apply { this.longitude = longitude }
            fun label(label: String?) = apply { this.label = label }
            fun query(query: String?) = apply { this.query = query }
            fun zoom(zoom: Int) = apply { this.zoom = zoom }
            fun action(action: MapAction) = apply { this.action = action }

            fun build() = MapsIntent(latitude, longitude, label, query, zoom, action)
        }

        fun builder(coordinates: Coordinates) = Builder(coordinates.latitude, coordinates.longitude)
        fun builder(latitude: Double, longitude: Double) = Builder(latitude, longitude)
    }
}
