package com.salman.klivvrandroidchallenge.presentation.composable

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.salman.klivvrandroidchallenge.domain.model.ImageResource
import androidx.compose.foundation.Image as ComposeImage

/**
 * Created by Muhammed Salman email(mahmadslman@gmail.com) on 5/23/2025.
 */

/**
 * A composable function that displays an image based on the provided [ImageResource].
 */
@Composable
fun Image(
    modifier: Modifier = Modifier,
    resource: ImageResource,
    contentScale: ContentScale = ContentScale.Fit,
    contentDescription: String? = null
) {

    when (resource) {
         is ImageResource.Res -> {
             ComposeImage(
                 modifier = modifier,
                 painter = painterResource(resource.resId),
                 contentDescription = contentDescription,
                 contentScale = contentScale
             )
         }
    }
}