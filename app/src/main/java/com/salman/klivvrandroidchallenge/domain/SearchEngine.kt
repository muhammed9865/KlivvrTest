package com.salman.klivvrandroidchallenge.domain

/**
 * Created by Muhammed Salman email(mahmadslman@gmail.com) on 5/23/2025.
 *
 * Interface for a search engine that searches for items of type T based on a query string.
 */
interface SearchEngine<T> {

    suspend fun search(items: List<T>, query: String): List<T>
}