package com.salman.klivvrandroidchallenge.presentation.model

/**
 * Created by Muhammed Salman email(mahmadslman@gmail.com) on 5/25/2025.
 *
 * Stores the scroll position of a timeline list.
 *
 * Used to sync the scroll positions across un-interchangeable Lazy List states.
 */
data class TimelineScrollPosition(
    val listIndex: Int = 0,
    val listOffset: Int = 0,
)

