package com.salman.klivvrandroidchallenge.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.salman.klivvrandroidchallenge.R
import com.salman.klivvrandroidchallenge.domain.model.CityItem
import com.salman.klivvrandroidchallenge.domain.model.LoadState
import com.salman.klivvrandroidchallenge.presentation.composable.CityTimelineList
import com.salman.klivvrandroidchallenge.presentation.composable.MapActionsSheet
import com.salman.klivvrandroidchallenge.presentation.composable.SearchBar
import com.salman.klivvrandroidchallenge.presentation.map.MapAction

/**
 * Created by Muhammed Salman email(mahmadslman@gmail.com) on 5/24/2025.
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
    onOpenCityDetails: (CityItem, MapAction) -> Unit = { _, _ -> }
) {
    val state = viewModel.state.collectAsStateWithLifecycle()
    val bottomSheetState = rememberModalBottomSheetState()
    HomeContent(
        modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        state = state.value,
        onQueryChanged = viewModel::onSearchQueryChanged,
        onSearchBarExpanded = viewModel::onSearchBarExpanded,
        onOpenCityDetails = viewModel::openMapActionsFor
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
    lazyListState: LazyListState = rememberLazyListState(),
    onOpenCityDetails: (CityItem) -> Unit = {},
    onQueryChanged: (String) -> Unit = {},
    onSearchBarExpanded: (Boolean) -> Unit = {}
) {
    val focusManager = LocalFocusManager.current
    Column(
        modifier = modifier
            .clickable(enabled = state.isSearchBarExpanded) {
                focusManager.clearFocus(force = true)
            }
            .padding(top = 16.dp)
    ) {
        ResultCount(countState = state.citiesCount)
        Box(
            modifier = Modifier
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
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
                        onCityClick = onOpenCityDetails
                    )
                }
        }
        SearchBar(
            query = state.searchQuery,
            isExpanded = state.isSearchBarExpanded,
            onQueryChanged = onQueryChanged,
            onExpandedChange = onSearchBarExpanded
        )
    }
}

@Composable
private fun ResultCount(
    modifier: Modifier = Modifier,
    countState: LoadState<Int>
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
    Text(
        modifier = modifier
            .fillMaxWidth(),
        textAlign = TextAlign.Center,
        text = text
    )
}

@Composable
private fun LoadingCities(modifier: Modifier = Modifier) {
    LinearProgressIndicator(modifier)
}




