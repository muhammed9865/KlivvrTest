package com.salman.klivvrandroidchallenge.data.search

import com.salman.klivvrandroidchallenge.domain.SearchEngine
import com.salman.klivvrandroidchallenge.domain.model.CityItem
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

/**
 * Created by Muhammed Salman email(mahmadslman@gmail.com) on 5/23/2025.
 *
 * This engine uses Binary search algorithm.
 * It uses [CityItem.searchKey] as a key to match with the query.
 *
 * The input items must be sorted using the same key.
 * Otherwise, the search result will be incorrect.
 */
import kotlinx.coroutines.withContext
import kotlin.coroutines.coroutineContext

class CitySearchEngine : SearchEngine<CityItem> {

    override suspend fun search(items: List<CityItem>, query: String): List<CityItem> = withContext(coroutineContext) {
        if (query.isBlank()) return@withContext items

        val firstIndex = items.lowerBound(query)
        if (firstIndex == -1) return@withContext emptyList()

        val lastIndex = items.upperBound(query, firstIndex)

        return@withContext items.subList(firstIndex, lastIndex)
    }

    private fun List<CityItem>.lowerBound(prefix: String): Int {
        var low = 0
        var high = lastIndex
        var result = -1
        while (low <= high) {
            val mid = (low + high) / 2
            val midValue = this[mid]

            if (midValue.searchKey.regionMatches(0, prefix, 0, prefix.length, ignoreCase = true)) {
                result = mid
                high = mid - 1
            } else if (midValue.searchKey.compareTo(prefix, ignoreCase = true) < 0) {
                low = mid + 1
            } else {
                high = mid - 1
            }
        }
        return result
    }

    private fun List<CityItem>.upperBound(prefix: String, start: Int): Int {
        var low = start
        var high = size
        while (low < high) {
            val mid = (low + high) / 2
            if (mid < size && this[mid].searchKey.startsWith(prefix, ignoreCase = true)) {
                low = mid + 1
            } else {
                high = mid
            }
        }
        return low
    }
}