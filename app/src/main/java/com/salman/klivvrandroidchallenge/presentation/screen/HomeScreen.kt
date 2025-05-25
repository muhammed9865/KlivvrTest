package com.salman.klivvrandroidchallenge.presentation.screen

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import com.salman.klivvrandroidchallenge.presentation.model.TimelineScrollPosition
import com.salman.klivvrandroidchallenge.presentation.theme.KlivvrAndroidChallengeTheme

/**
 * Created by Muhammed Salman email(mahmadslman@gmail.com) on 5/25/2025.
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
        timelineScrollPosition = viewModel.timelineScrollPosition,
        onTimelineScrollChanged = viewModel::updateTimelineScrollPosition
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
    timelineScrollPosition: TimelineScrollPosition = TimelineScrollPosition(),
    onOpenCityDetails: (CityItem) -> Unit = {},
    onQueryChanged: (String) -> Unit = {},
    onSearchBarExpanded: (Boolean) -> Unit = {},
    onToggleNightMode: () -> Unit = {},
    onTimelineScrollChanged: (TimelineScrollPosition) -> Unit = {}
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
    Scaffold(
        modifier = modifier,
        bottomBar = {
            SearchBar(
                modifier = Modifier
                    .navigationBarsPadding()
                    .alpha(animatedSearchBarAlpha),
                query = state.searchQuery,
                isExpanded = state.isSearchBarExpanded,
                onQueryChanged = onQueryChanged,
                onExpandedChange = onSearchBarExpanded
            )
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable(enabled = state.isSearchBarExpanded) {
                        focusManager.clearFocus(force = true)
                    },
                verticalArrangement = Arrangement.Center
            ) {
                TopBar(
                    modifier = Modifier.statusBarsPadding(),
                    countState = state.citiesCount,
                    isNightMode = isNightMode,
                    onToggleNightMode = onToggleNightMode
                )
                Box(
                    Modifier
                        .weight(1f)
                        .fillMaxWidth(), contentAlignment = Alignment.Center
                ) {
                    state.groupOfCities
                        .onLoading {
                            LoadingCities(
                                modifier = Modifier
                                    .fillMaxWidth(0.7f)
                                    .align(Alignment.Center)
                            )
                        }
                        .onData {
                            CityTimelineList(
                                modifier = Modifier.padding(
                                    horizontal = 16.dp,
                                ),
                                groups = it,
                                lazyListState = lazyListState,
                                lazyGridState = lazyGridState,
                                onCityClick = onOpenCityDetails,
                                isPortrait = isPortrait,
                                contentPadding = innerPadding,
                                scrollPosition = timelineScrollPosition,
                                onScrollChanged = onTimelineScrollChanged
                            )
                        }
                }
            }
        }
    )
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

@Preview(name = "EnhancedHomeScreen Portrait", showBackground = true, widthDp = 360, heightDp = 800)
@Composable
fun PreviewEnhancedHomeScreenPortrait() {
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

@Preview(
    name = "EnhancedHomeScreen Landscape",
    showBackground = true,
    widthDp = 800,
    heightDp = 360
)
@Composable
fun PreviewEnhancedHomeScreenLandscape() {
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

