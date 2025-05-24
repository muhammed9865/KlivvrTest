package com.salman.klivvrandroidchallenge.data.search

import com.salman.klivvrandroidchallenge.domain.SearchEngine
import com.salman.klivvrandroidchallenge.domain.model.CityItem

/**
 * Created by Muhammed Salman email(mahmadslman@gmail.com) on 5/23/2025.
 *
 * This engine uses Binary search algorithm.
 * It uses [CityItem.searchKey] as a key to match with the query.
 *
 * The input items must be sorted using the same key.
 * Otherwise, the search result will be incorrect.
 */
class CitySearchEngine: SearchEngine<CityItem> {

    override fun search(items: List<CityItem>, query: String): List<CityItem> {
        if (query.isBlank()) return items
        val searchKeys = items.map { it.searchKey }
        val firstIndex = searchKeys.lowerBound(query)
        if (firstIndex == -1) return emptyList()
        val lastIndex = searchKeys.upperBound(query, firstIndex)
        return items.subList(firstIndex, lastIndex)
    }

    /**
     * Finds the first index where the prefix could match (lower bound).
     * Returns -1 if no such index exists.
     */
    private fun List<String>.lowerBound(prefix: String): Int {
        var low = 0
        var high = lastIndex
        var result = -1
        while (low <= high) {
            val mid = (low + high) / 2
            val midValue = this[mid]
            if (midValue.regionMatches(0, prefix, 0, prefix.length, ignoreCase = true)) {
                result = mid
                high = mid - 1 // look for earlier match
            } else if (midValue.compareTo(prefix, ignoreCase = true) < 0) {
                low = mid + 1
            } else {
                high = mid - 1
            }
        }
        return result
    }

    /**
     * Finds the first index where the prefix no longer matches (upper bound), starting from lowerBound.
     * Returns items.size if all remaining items match.
     */
    private fun List<String>.upperBound(prefix: String, start: Int): Int {
        var low = start
        var high = size
        while (low < high) {
            val mid = (low + high) / 2
            if (mid < size && this[mid].startsWith(prefix, ignoreCase = true)) {
                low = mid + 1
            } else {
                high = mid
            }
        }
        return low
    }
}
