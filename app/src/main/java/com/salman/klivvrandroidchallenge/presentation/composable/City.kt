package com.salman.klivvrandroidchallenge.presentation.composable

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.salman.klivvrandroidchallenge.R
import com.salman.klivvrandroidchallenge.domain.model.CityItem
import com.salman.klivvrandroidchallenge.domain.model.Coordinates
import com.salman.klivvrandroidchallenge.domain.model.ImageResource
import com.salman.klivvrandroidchallenge.presentation.theme.KlivvrAndroidChallengeTheme

/**
 * Created by Muhammed Salman email(mahmadslman@gmail.com) on 5/24/2025.
 */


@Composable
fun CityCard(
    modifier: Modifier = Modifier,
    item: CityItem,
    onClick: (CityItem) -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.medium)
            .background(MaterialTheme.colorScheme.surface)
            .clickable(onClick = { onClick(item) })
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CountryImage(item.image)
        Spacer(Modifier.width(16.dp))
        CityDetails(item)

    }
}

@Composable
private fun CountryImage(
    image: ImageResource,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .width(84.dp)
            .aspectRatio(1f)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Image(
            modifier = Modifier
                .fillMaxSize()
                .clip(MaterialTheme.shapes.extraSmall)
                .aspectRatio(2 / 1f),
            resource = image,
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
private fun CityDetails(item: CityItem, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = "${item.name}, ${item.country}",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = item.coordinatesString,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
    }
}

@Preview(
    showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO,
)
@Composable
private fun CityCardPreview() {
    KlivvrAndroidChallengeTheme {
        CityCard(
            item = CityItem(
                id = 1,
                name = "Cairo",
                country = "EG",
                image = ImageResource.Res(R.drawable.flag_ad),
                coordinates = Coordinates(
                    30.0444,
                    31.2357
                ),
            ),
        ) {

        }
    }
}

@Preview(
    showBackground = true, uiMode = Configuration.ORIENTATION_LANDSCAPE,
    widthDp = 840
)
@Composable
private fun CityCardLandscapePreview() {
    KlivvrAndroidChallengeTheme {
        CityCard(
            item = CityItem(
                id = 1,
                name = "Cairo",
                country = "EG",
                image = ImageResource.Res(R.drawable.flag_ad),
                coordinates = Coordinates(
                    30.0444,
                    31.2357
                ),
            ),
        ) {

        }
    }
}

