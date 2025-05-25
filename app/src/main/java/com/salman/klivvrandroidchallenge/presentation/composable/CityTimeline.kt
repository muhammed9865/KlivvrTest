package com.salman.klivvrandroidchallenge.presentation.composable

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.salman.klivvrandroidchallenge.domain.model.CityItem
import com.salman.klivvrandroidchallenge.domain.model.GroupOfCity
import com.salman.klivvrandroidchallenge.presentation.model.TimelineScrollPosition

/**
 * Created by Muhammed Salman email(mahmadslman@gmail.com) on 5/24/2025.
 */

/**
 * Syncs the scroll position of a LazyListState or LazyGridState
 * based on the current orientation.
 *
 * This composable will restore the scroll position
 * when the orientation changes and will store the current
 * scroll position on every scroll event.
 *
 * @param isPortrait Boolean indicating if the current orientation is portrait.
 * @param lazyListState The LazyListState to sync when in portrait mode.
 * @param lazyGridState The LazyGridState to sync when in landscape mode.
 * @param scrollPosition The current scroll position to restore.
 * @param onScrollChanged Callback to notify when the scroll position changes.
 */
@Composable
fun ScrollPositionSync(
    isPortrait: Boolean,
    lazyListState: LazyListState,
    lazyGridState: LazyGridState,
    scrollPosition: TimelineScrollPosition = TimelineScrollPosition(),
    onScrollChanged: (TimelineScrollPosition) -> Unit = {}
) {
    // Restore scroll position on first composition
    LaunchedEffect(isPortrait) {
        if (isPortrait) {
            lazyListState.scrollToItem(scrollPosition.listIndex, scrollPosition.listOffset)
        } else {
            lazyGridState.scrollToItem(scrollPosition.listIndex, scrollPosition.listOffset)
        }
    }

    // Store the scroll positions on orientation change
    DisposableEffect(isPortrait) {
        onDispose {
            if (isPortrait) {
                onScrollChanged(
                    scrollPosition.copy(
                        listIndex = lazyListState.firstVisibleItemIndex,
                        listOffset = lazyListState.firstVisibleItemScrollOffset
                    )
                )
            } else {
                onScrollChanged(
                    scrollPosition.copy(
                        listIndex = lazyGridState.firstVisibleItemIndex,
                        listOffset = lazyGridState.firstVisibleItemScrollOffset
                    )
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CityTimelineList(
    modifier: Modifier = Modifier,
    groups: List<GroupOfCity>,
    lazyListState: LazyListState,
    lazyGridState: LazyGridState = rememberLazyGridState(),
    contentPadding: PaddingValues = PaddingValues(0.dp),
    isPortrait: Boolean = LocalConfiguration.current.orientation == Configuration.ORIENTATION_PORTRAIT,
    onCityClick: (CityItem) -> Unit,
    scrollPosition: TimelineScrollPosition = TimelineScrollPosition(),
    onScrollChanged: (TimelineScrollPosition) -> Unit = {}
) {
    val lastItem = remember(groups.size) {
        if (isPortrait) {
            groups.lastOrNull()?.cities?.lastOrNull()
        } else {
            groups.lastOrNull()?.cities?.let { cities ->
                if (cities.size > 1) {
                    cities[cities.size - 2] // Get the second last item
                } else {
                    cities.lastOrNull() // Fallback to last item if only one exists
                }
            }
        }
    }

    // Use the scroll position sync helper
    ScrollPositionSync(
        isPortrait = isPortrait,
        lazyListState = lazyListState,
        lazyGridState = lazyGridState,
        scrollPosition = scrollPosition,
        onScrollChanged = onScrollChanged
    )

    if (isPortrait) {
        PortraitTimelineList(
            modifier = modifier,
            groups = groups,
            lazyListState = lazyListState,
            contentPadding = contentPadding,
            lastItem = lastItem,
            onCityClick = onCityClick
        )
    } else {
        LandscapeTimelineGrid(
            modifier = modifier,
            groups = groups,
            lastItem = lastItem,
            lazyGridState = lazyGridState,
            contentPadding = contentPadding,
            onCityClick = onCityClick
        )
    }
}

@Composable
private fun PortraitTimelineList(
    modifier: Modifier = Modifier,
    groups: List<GroupOfCity>,
    lazyListState: LazyListState,
    lastItem: CityItem?,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    onCityClick: (CityItem) -> Unit
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        state = lazyListState,
        contentPadding = contentPadding
    ) {
        groups.forEach { group ->
            stickyHeader {
                TimelineRowHeader(
                    character = group.startsByCharacter
                )
            }
            itemsIndexed(group.cities, key = { _, item -> item.id }) { _, city ->
                TimelineRowItem(
                    city = city,
                    isLastItem = lastItem == city,
                    isPortrait = true,
                    onClick = onCityClick
                )
            }
        }
    }
}

@Composable
private fun LandscapeTimelineGrid(
    modifier: Modifier = Modifier,
    groups: List<GroupOfCity>,
    lazyGridState: LazyGridState = rememberLazyGridState(),
    lastItem: CityItem?,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    onCityClick: (CityItem) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier.fillMaxSize(),
        state = lazyGridState,
        contentPadding = contentPadding,
    ) {
        groups.forEach { group ->
            stickyHeader {
                TimelineRowHeader(character = group.startsByCharacter)
            }
            itemsIndexed(group.cities, key = { _, city -> city.id }) { idx, city ->
                TimelineRowItem(
                    city = city,
                    isLastItem = lastItem == city,
                    showIndicator = idx % 2 == 0, // Show indicator for every first item in a pair
                    isPortrait = false,
                    onClick = onCityClick
                )
            }
        }
    }
}

@Composable
fun TimelineRowHeader(
    modifier: Modifier = Modifier,
    character: Char
) {
    Column {
        Box(
            modifier
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surface)
                .border(2.dp, MaterialTheme.colorScheme.outline, CircleShape)
                .size(64.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = character.toString(),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }
    }
}

@Composable
fun TimelineRowItem(
    modifier: Modifier = Modifier,
    city: CityItem,
    isLastItem: Boolean = true,
    showIndicator: Boolean = true,
    isPortrait: Boolean,
    onClick: (CityItem) -> Unit
) {
    ConstraintLayout(
        modifier = modifier.fillMaxWidth()
    ) {
        val (indicator, card) = createRefs()

        if (showIndicator) {
            TimelineIndicator(
                modifier = Modifier
                    .constrainAs(indicator) {
                        start.linkTo(parent.start, margin = if (isLastItem) 24.dp else 32.dp)
                        top.linkTo(card.top)
                        bottom.linkTo(card.bottom)
                        height = Dimension.fillToConstraints
                    },
                isLastIndicator = isLastItem
            )
        }
        val cardPaddingModifier = if (isPortrait) {
            Modifier.padding(vertical = 8.dp)
        } else {
            Modifier
                .padding(start = 24.dp)
                .padding(vertical = 8.dp)
        }
        CityCard(
            modifier = Modifier
                .constrainAs(card) {
                    if (isPortrait) {
                        start.linkTo(indicator.end, margin = 16.dp)
                    } else {
                        start.linkTo(parent.start, margin = 24.dp)
                    }
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    width = Dimension.fillToConstraints
                }
                .then(cardPaddingModifier),
            item = city,
            onClick = onClick
        )
    }

}

@Composable
fun TimelineIndicator(
    modifier: Modifier = Modifier,
    isLastIndicator: Boolean
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier
                .width(1.dp)
                .weight(1f)
                .background(MaterialTheme.colorScheme.outline)
        )
        if (isLastIndicator) {
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .size(16.dp)
                    .background(MaterialTheme.colorScheme.outline)
            )
        }

    }
}

