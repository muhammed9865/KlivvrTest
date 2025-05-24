package com.salman.klivvrandroidchallenge.data.source

import com.salman.klivvrandroidchallenge.domain.model.ImageResource

/**
 * Created by Muhammed Salman email(mahmadslman@gmail.com) on 5/24/2025.
 */
interface CountryImageSource {

    /**
     * Returns an [ImageResource] for the given country name.
     *
     * @param country The 2-letter country code or name.
     * e.g. "US" for "United States".
     */
    fun getCountryImage(country: String): ImageResource
}