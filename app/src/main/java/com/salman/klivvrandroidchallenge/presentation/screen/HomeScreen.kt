package com.salman.klivvrandroidchallenge.presentation.screen

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.salman.klivvrandroidchallenge.R
import com.salman.klivvrandroidchallenge.domain.model.CityItem
import com.salman.klivvrandroidchallenge.domain.model.LoadState
import com.salman.klivvrandroidchallenge.presentation.composable.CityTimelineList
import com.salman.klivvrandroidchallenge.presentation.composable.MapActionsSheet
import com.salman.klivvrandroidchallenge.presentation.composable.NightModeSwitch
import com.salman.klivvrandroidchallenge.presentation.composable.SearchBar
import com.salman.klivvrandroidchallenge.presentation.isPortraitMode
import com.salman.klivvrandroidchallenge.presentation.map.MapAction
import com.salman.klivvrandroidchallenge.presentation.theme.KlivvrAndroidChallengeTheme

/**
 * Created by Muhammed Salman email(mahmadslman@gmail.com) on 5/24/2025.
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    isNightMode: Boolean,
    viewModel: HomeViewModel = hiltViewModel(),
    onToggleNightMode: () -> Unit = {},
    onOpenCityDetails: (CityItem, MapAction) -> Unit = { _, _ -> }
) {
    val state = viewModel.state.collectAsStateWithLifecycle()
    val bottomSheetState = rememberModalBottomSheetState()
    HomeContent(
        modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        state = state.value,
        isNightMode = isNightMode,
        onQueryChanged = viewModel::onSearchQueryChanged,
        onSearchBarExpanded = viewModel::onSearchBarExpanded,
        onOpenCityDetails = viewModel::openMapActionsFor,
        onToggleNightMode = onToggleNightMode,
    )
    if (state.value.showCityItemMapActions != null) {
        MapActionsSheet(
            onDismiss = viewModel::hideMapActions,
            cityItem = state.value.showCityItemMapActions,
            sheetState = bottomSheetState,
            onAction = onOpenCityDetails
        )
    }
}

@Composable
private fun HomeContent(
    modifier: Modifier = Modifier,
    state: HomeState,
    isNightMode: Boolean,
    isPortrait: Boolean = isPortraitMode(),
    lazyListState: LazyListState = rememberLazyListState(),
    lazyGridState: LazyGridState = rememberLazyGridState(),
    onOpenCityDetails: (CityItem) -> Unit = {},
    onQueryChanged: (String) -> Unit = {},
    onSearchBarExpanded: (Boolean) -> Unit = {},
    onToggleNightMode: () -> Unit = {}
) {
    val focusManager = LocalFocusManager.current
    val isScrolling by remember {
        derivedStateOf {
            lazyListState.isScrollInProgress || lazyGridState.isScrollInProgress
        }
    }
    val animatedSearchBarAlpha by animateFloatAsState(
        targetValue = if (isScrolling) 0.5f else 1f,
        label = "searchBarAlpha"
    )
    Box(
        modifier = modifier
            .fillMaxSize()
            .clickable(enabled = state.isSearchBarExpanded) {
                focusManager.clearFocus(force = true)
            },
        contentAlignment = Alignment.Center
    ) {
        var searchBarHeight by remember { mutableStateOf(0.dp) }
        var topBarHeight by remember { mutableStateOf(0.dp) }
        state.groupOfCities
            .onLoading {
                LoadingCities(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                )
            }
            .onData {
                CityTimelineList(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    groups = it,
                    lazyListState = lazyListState,
                    lazyGridState = lazyGridState,
                    onCityClick = onOpenCityDetails,
                    contentPadding = PaddingValues(
                        bottom = searchBarHeight + 16.dp,
                        top = topBarHeight / 2f
                    ),
                    isPortrait = isPortrait
                )
            }
        TopBar(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopStart)
                .onGloballyPositioned {
                    topBarHeight = it.size.height.dp
                },
            countState = state.citiesCount,
            isNightMode = isNightMode,
            onToggleNightMode = onToggleNightMode
        )
        SearchBar(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .alpha(animatedSearchBarAlpha)
                .onGloballyPositioned {
                    searchBarHeight = it.size.height.dp
                },
            query = state.searchQuery,
            isExpanded = state.isSearchBarExpanded,
            onQueryChanged = onQueryChanged,
            onExpandedChange = onSearchBarExpanded
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(
    modifier: Modifier = Modifier,
    isNightMode: Boolean = isSystemInDarkTheme(),
    countState: LoadState<Int>,
    onToggleNightMode: () -> Unit = {}
) {
    val text = when (countState) {
        is LoadState.Loading -> stringResource(R.string.loading)
        is LoadState.Error -> stringResource(R.string.failed_to_load_cities)
        is LoadState.Success -> pluralStringResource(
            R.plurals.cities_count,
            countState.data,
            countState.data
        )

        is LoadState.Idle -> stringResource(R.string.search_for_cities)
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = stringResource(R.string.city_search),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground,
        )
        Text(
            modifier = Modifier
                .padding(start = 8.dp),
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground,
            maxLines = 1,
            softWrap = false,
        )
        NightModeSwitch(
            isDarkMode = isNightMode,
            onToggle = onToggleNightMode,
        )
    }
}

@Composable
private fun LoadingCities(modifier: Modifier = Modifier) {
    LinearProgressIndicator(modifier)
}

@Preview(name = "HomeScreen Portrait", showBackground = true, widthDp = 360, heightDp = 800)
@Composable
fun PreviewHomeScreenPortrait() {
    KlivvrAndroidChallengeTheme {
        var expanded by remember { mutableStateOf(false) }
        HomeContent(
            state = HomeState.preview(expanded),
            isPortrait = true,
            onSearchBarExpanded = { expanded = it },
            isNightMode = isSystemInDarkTheme()
        )
    }
}

@Preview(name = "HomeScreen Landscape", showBackground = true, widthDp = 800, heightDp = 360)
@Composable
fun PreviewHomeScreenLandscape() {
    KlivvrAndroidChallengeTheme {
        var expanded by remember { mutableStateOf(false) }
        HomeContent(
            state = HomeState.preview(expanded),
            isPortrait = false,
            onSearchBarExpanded = { expanded = it },
            isNightMode = isSystemInDarkTheme()
        )
    }
}
