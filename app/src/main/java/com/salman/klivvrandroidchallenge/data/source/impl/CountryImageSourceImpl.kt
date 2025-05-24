package com.salman.klivvrandroidchallenge.data.source.impl

import android.content.Context
import com.salman.klivvrandroidchallenge.R
import com.salman.klivvrandroidchallenge.data.source.CountryImageSource
import com.salman.klivvrandroidchallenge.domain.model.ImageResource
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

/**
 * Created by Muhammed Salman email(mahmadslman@gmail.com) on 5/24/2025.
 */
class CountryImageSourceImpl @Inject constructor(
    @ApplicationContext private val context: Context
): CountryImageSource {

    override fun getCountryImage(country: String): ImageResource {
        return ImageResource.Res(getImageResourceId(country))
    }

    private fun getImageResourceId(country: String): Int {
        val resourceName = "flag_" + country.lowercase()
        val resId = context.resources.getIdentifier(resourceName, "drawable", context.packageName)
        return if (resId == 0) {
            R.drawable.flag_eg
        } else {
            resId
        }
    }
}

