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
        val searchKeys = items.map { it.searchKey }
        val resultFirstIndex = searchKeys.binarySearch(query, findFirstItem = true)
        val resultLastIndex = searchKeys.binarySearch(query, findFirstItem = false)
        return if (resultFirstIndex == -1 || resultLastIndex == -1) {
            emptyList()
        } else {
            items.subList(resultFirstIndex, resultLastIndex + 1)
        }
    }

    /**
     * Iterates through the list in a non-linear way (Binary search).
     *
     * It finds either the very first or the very last item that matches the query.
     *
     * @param query The query to search for.
     * @param findFirstItem If true, it finds the first item that matches the query.
     * If false, it finds the last item that matches the query.
     *
     * @return The index of the item that matches the query.
     * If not found, returns -1.
     */
    private fun List<String>.binarySearch(query: String, findFirstItem: Boolean): Int {
        var low = 0
        var high = lastIndex
        var result = -1
        while (low <= high) {
            val mid = (low + high) / 2
            val midValue = this[mid]

            if (midValue == query) {
                result = mid
                if (findFirstItem) {
                    high = mid - 1 // move left to find the first occurrence
                } else {
                    low = mid + 1 // move right to find the last occurrence
                }
            } else if (midValue < query) {
                low = mid + 1 // Search in the right half
            } else {
                high = mid - 1 // Search in the left half
            }
        }
        return result
    }
}