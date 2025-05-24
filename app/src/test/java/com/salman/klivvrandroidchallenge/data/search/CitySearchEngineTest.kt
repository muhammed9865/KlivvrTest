package com.salman.klivvrandroidchallenge.data.search

import com.salman.klivvrandroidchallenge.domain.SearchEngine
import com.salman.klivvrandroidchallenge.domain.model.CityItem
import com.salman.klivvrandroidchallenge.domain.model.ImageResource
import com.salman.klivvrandroidchallenge.domain.model.Coordinates
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class CitySearchEngineTest {

    private lateinit var searchEngine: SearchEngine<CityItem>
    private lateinit var cities: List<CityItem>

    @Before
    fun setUp() {
        searchEngine = CitySearchEngine()
        cities = listOf(
            CityItem(1, "Alabama", "US", ImageResource.Res(0), Coordinates(0.0, 0.0)),
            CityItem(2, "Albuquerque", "US", ImageResource.Res(0), Coordinates(0.0, 0.0)),
            CityItem(3, "Anaheim", "US", ImageResource.Res(0), Coordinates(0.0, 0.0)),
            CityItem(4, "Arizona", "US", ImageResource.Res(0), Coordinates(0.0, 0.0)),
            CityItem(5, "Sydney", "AU", ImageResource.Res(0), Coordinates(0.0, 0.0))
        ).sortedBy { it.searchKey }
    }

    @Test
    fun `search with prefix A returns all except Sydney`() {
        val result = searchEngine.search(cities, "A")
        assertEquals(listOf("Alabama", "Albuquerque", "Anaheim", "Arizona"), result.map { it.name })
    }

    @Test
    fun `search with prefix s returns only Sydney`() {
        val result = searchEngine.search(cities, "s")
        assertEquals(listOf("Sydney"), result.map { it.name })
    }

    @Test
    fun `search with prefix Al returns Alabama and Albuquerque`() {
        val result = searchEngine.search(cities, "Al")
        assertEquals(listOf("Alabama", "Albuquerque"), result.map { it.name })
    }

    @Test
    fun `search with prefix Alb returns only Albuquerque`() {
        val result = searchEngine.search(cities, "Alb")
        assertEquals(listOf("Albuquerque"), result.map { it.name })
    }

    @Test
    fun `search with empty prefix returns all`() {
        val result = searchEngine.search(cities, "")
        assertEquals(cities.map { it.name }, result.map { it.name })
    }

    @Test
    fun `search with non-matching prefix returns empty`() {
        val result = searchEngine.search(cities, "Z")
        assertEquals(emptyList<String>(), result.map { it.name })
    }

    @Test
    fun `search with mixed case prefix returns match`() {
        val result = searchEngine.search(cities, "aLa")
        assertEquals(listOf("Alabama"), result.map { it.name })
    }

    @Test
    fun `search with full city name returns exact match`() {
        val result = searchEngine.search(cities, "Albuquerque")
        assertEquals(listOf("Albuquerque"), result.map { it.name })
    }

    @Test
    fun `search with prefix longer than any name returns empty`() {
        val result = searchEngine.search(cities, "Alabamazzz")
        assertEquals(emptyList<String>(), result.map { it.name })
    }

    @Test
    fun `search with non-existing in-between prefix returns empty`() {
        val result = searchEngine.search(cities, "Alz")
        assertEquals(emptyList<String>(), result.map { it.name })
    }

    @Test
    fun `search with prefix that matches only one city in the middle`() {
        val result = searchEngine.search(cities, "An")
        assertEquals(listOf("Anaheim"), result.map { it.name })
    }

    @Test
    fun `search where match is the first city in list`() {
        val result = searchEngine.search(cities, "Alabama")
        assertEquals(listOf("Alabama"), result.map { it.name })
    }

    @Test
    fun `search where match is the last city in list`() {
        val result = searchEngine.search(cities, "Sydney")
        assertEquals(listOf("Sydney"), result.map { it.name })
    }

    @Test
    fun `search with single city list and matching prefix`() {
        val single = listOf(CityItem(99, "Cairo", "EG", ImageResource.Res(0), Coordinates(0.0, 0.0)))
        val result = searchEngine.search(single, "Ca")
        assertEquals(listOf("Cairo"), result.map { it.name })
    }

    @Test
    fun `search with space-only prefix returns all`() {
        val result = searchEngine.search(cities, "   ")
        assertEquals(cities.map { it.name }, result.map { it.name })
    }

    @Test
    fun `search with redundant names returns all`() {
        val redundantCities = listOf(
            CityItem(1, "Sydney", "AU", ImageResource.Res(0), Coordinates(0.0, 0.0)),
            CityItem(2, "Sydney", "AU", ImageResource.Res(0), Coordinates(0.0, 0.0)),
        )
        val result = searchEngine.search(redundantCities, "Sydney")
        assertEquals(redundantCities.map { it.name }, result.map { it.name })
    }
}

