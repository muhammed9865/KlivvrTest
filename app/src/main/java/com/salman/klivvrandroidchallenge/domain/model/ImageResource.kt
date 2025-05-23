package com.salman.klivvrandroidchallenge.domain.model

/**
 * Created by Muhammed Salman email(mahmadslman@gmail.com) on 5/23/2025.
 *
 * Container class for an image asset.
 *
 * Currently supports:
 * - Resource ID [Res]
 *
 * Future support:
 * - Image URL (requires network access/ 3rd party library to load image efficiently)
 */
sealed interface ImageResource {

    data class Res(val resId: Int) : ImageResource

}