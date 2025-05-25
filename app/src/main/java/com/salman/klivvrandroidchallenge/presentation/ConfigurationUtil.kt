package com.salman.klivvrandroidchallenge.presentation

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration

/**
 * Created by Muhammed Salman email(mahmadslman@gmail.com) on 5/25/2025.
 */


@Composable
fun isPortraitMode(): Boolean {
    return LocalConfiguration.current.orientation == Configuration.ORIENTATION_PORTRAIT
}