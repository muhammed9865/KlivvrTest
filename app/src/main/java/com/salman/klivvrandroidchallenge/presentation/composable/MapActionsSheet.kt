package com.salman.klivvrandroidchallenge.presentation.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.salman.klivvrandroidchallenge.domain.model.CityItem
import com.salman.klivvrandroidchallenge.domain.model.Coordinates
import com.salman.klivvrandroidchallenge.domain.model.ImageResource
import com.salman.klivvrandroidchallenge.presentation.map.MapAction
import com.salman.klivvrandroidchallenge.presentation.theme.KlivvrAndroidChallengeTheme

/**
 * Created by Muhammed Salman email(mahmadslman@gmail.com) on 5/25/2025.
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapActionsSheet(
    modifier: Modifier = Modifier,
    cityItem: CityItem?,
    sheetState: SheetState = rememberModalBottomSheetState(),
    mapActions: List<MapAction> = MapAction.entries,
    onAction: (CityItem, MapAction) -> Unit = { _, _ -> },
    onDismiss: () -> Unit = {},
) {
    ModalBottomSheet(
        onDismiss,
        sheetState = sheetState,
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(16.dp)
            ,
            horizontalArrangement = Arrangement.SpaceAround,
        ) {
            mapActions.forEach {
                MapActionCard(
                    modifier = Modifier.weight(1f),
                    mapAction = it,
                    onAction = { action ->
                        if (cityItem != null) {
                            onAction(cityItem, action)
                        }
                        onDismiss()
                    }
                )
            }
        }
    }
}

@Composable
private fun MapActionCard(
    modifier: Modifier = Modifier,
    mapAction: MapAction,
    onAction: (MapAction) -> Unit = { _ -> }
) {

    Column(
        modifier = modifier
            .clip(MaterialTheme.shapes.medium)
            .clickable { onAction(mapAction) }
            .padding(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Image(
            painter = painterResource(mapAction.iconResId),
            contentDescription = stringResource(mapAction.titleResId),
        )
        Text(
            text = stringResource(mapAction.titleResId),
            modifier = Modifier.padding(horizontal = 8.dp),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
private fun PreviewMapActionCard() {
    KlivvrAndroidChallengeTheme {
        Column {
            val sheetState = rememberStandardBottomSheetState(
                initialValue = SheetValue.Expanded
            )
            MapActionsSheet(
                cityItem = CityItem(
                    id = 1,
                    name = "Sample City",
                    country = "Sample Country",
                    image = ImageResource.Res(0),
                    coordinates = Coordinates(0.0, 0.0)
                ),
                sheetState = sheetState,
            )
        }
    }
}