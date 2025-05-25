package com.salman.klivvrandroidchallenge.presentation.composable

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.salman.klivvrandroidchallenge.R
import com.salman.klivvrandroidchallenge.presentation.isPortraitMode
import com.salman.klivvrandroidchallenge.presentation.theme.KlivvrAndroidChallengeTheme

/**
 * Created by Muhammed Salman email(mahmadslman@gmail.com) on 5/24/2025.
 */
@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    isExpanded: Boolean,
    isPortrait: Boolean = isPortraitMode(),
    onExpandedChange: (Boolean) -> Unit,
    query: String?,
    onQueryChanged: (String) -> Unit
) {
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    // Portrait uses tighter horizontal padding; landscape gives more breathing room
    val targetVertical = if (isExpanded) 0.dp else 12.dp
    val targetHorizontal = if (isExpanded) {
        if (isPortrait) 0.dp else 48.dp
    } else {
        if (isPortrait) 16.dp else 64.dp
    }

    val animatedVertical by animateDpAsState(targetVertical, label = "verticalPadding")
    val animatedHorizontal by animateDpAsState(targetHorizontal, label = "horizontalPadding")

    val widthModifier = if (isPortrait) {
        Modifier.fillMaxWidth()
    } else {
        Modifier
            .fillMaxWidth(0.7f) 
    }

    Box(
        modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier
                .then(widthModifier)
                .clip(
                    shape = MaterialTheme.shapes.medium.copy(
                        bottomEnd = CornerSize(0f),
                        bottomStart = CornerSize(0f)
                    )
                )
                .background(MaterialTheme.colorScheme.surface)
                .padding(
                    vertical = animatedVertical,
                    horizontal = animatedHorizontal
                ),
        ) {
            val currentOnExpandedChange by rememberUpdatedState(onExpandedChange)
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester)
                    .onFocusChanged { focusState ->
                        currentOnExpandedChange(focusState.isFocused)
                    },
                value = query.orEmpty(),
                onValueChange = onQueryChanged,
                placeholder = {
                    Text(
                        stringResource(R.string.search),
                        style = MaterialTheme.typography.bodySmall
                    )
                },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = "Search cities")
                },
                trailingIcon = {
                    ClearIcon(query) {
                        onQueryChanged("")
                        focusManager.clearFocus(force = true)
                    }
                },
                keyboardActions = KeyboardActions(onSearch = {
                    onQueryChanged(query.orEmpty())
                    focusManager.clearFocus(force = true)
                }),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                shape = if (isExpanded) TextFieldDefaults.shape else MaterialTheme.shapes.extraLarge,
                colors = if (!isExpanded) {
                    TextFieldDefaults.colors(
                        unfocusedContainerColor = MaterialTheme.colorScheme.background,
                        focusedContainerColor = MaterialTheme.colorScheme.background,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                    )
                } else {
                    TextFieldDefaults.colors(
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                    )
                },
            )
        }
    }
}


@Composable
private fun ClearIcon(query: String?, onCLicked: () -> Unit) {
    val isRtl = LocalLayoutDirection.current == LayoutDirection.Rtl
    AnimatedVisibility(
        visible = !query.isNullOrBlank(),
        enter = slideInHorizontally {
            if (isRtl) -it else it
        },
        exit = slideOutHorizontally {
            if (isRtl) -it else it
        }
    ) {
        IconButton(onClick = {
            onCLicked()
        }) {
            Icon(
                Icons.Default.Clear,
                contentDescription = "Clear search query",
            )
        }
    }
}


@Preview
@Composable
private fun SearchBarPreview() {
    KlivvrAndroidChallengeTheme {
        Column {
            var state by remember { mutableStateOf("") }
            var expanded by remember { mutableStateOf(false) }
            SearchBar(
                query = state,
                onQueryChanged = { state = it },
                isExpanded = expanded,
                onExpandedChange = { expanded = it }
            )
            SearchBar(query = "", onQueryChanged = {}, isExpanded = false, onExpandedChange = {})
        }
    }
}