package com.salman.klivvrandroidchallenge.presentation

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

/**
 * Created by Muhammed Salman email(mahmadslman@gmail.com) on 5/25/2025.
 */
@HiltViewModel
class MainViewModel @Inject constructor(): ViewModel() {

    private val isDarkModeState = MutableStateFlow(false)
    val isDarkMode = isDarkModeState.asStateFlow()

    fun toggleDarkMode() {
        isDarkModeState.value = !isDarkModeState.value
    }
}