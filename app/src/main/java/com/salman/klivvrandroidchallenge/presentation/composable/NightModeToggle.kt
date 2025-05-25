package com.salman.klivvrandroidchallenge.presentation.composable

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.salman.klivvrandroidchallenge.R
import com.salman.klivvrandroidchallenge.presentation.theme.KlivvrAndroidChallengeTheme

/**
 * Created by Muhammed Salman email(mahmadslman@gmail.com) on 5/25/2025.
 */

@Composable
fun NightModeSwitch(
    isDarkMode: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    val transition = updateTransition(targetState = isDarkMode, label = "NightModeSwitch")

    val backgroundColor by transition.animateColor(label = "BackgroundColor") { dark ->
        if (dark) Color(0xFF2C2C2C) else Color(0xFFFFF176)
    }

    val iconColor by transition.animateColor(label = "IconColor") { dark ->
        if (dark) Color.White else Color(0xFFFFC107)
    }

    val layoutDirection = LocalLayoutDirection.current
    val offsetDp by transition.animateDp(label = "IconOffset") { dark ->
        if (dark) 24.dp else 0.dp
    }
    val effectiveOffset = if (layoutDirection == LayoutDirection.Rtl) offsetDp else -offsetDp

    Surface(
        modifier = modifier
            .size(width = 56.dp, height = 32.dp)
            .clip(RoundedCornerShape(16.dp))
            .clickable { onToggle() },
        color = backgroundColor,
        tonalElevation = 2.dp
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(4.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = if (isDarkMode) R.drawable.baseline_dark_mode_24 else R.drawable.baseline_light_mode_24),
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier
                    .size(24.dp)
                    .offset(x = effectiveOffset)
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun NightModeTogglePreview() {
    KlivvrAndroidChallengeTheme {
        var isDarkMode by remember { mutableStateOf(false) }
        NightModeSwitch(
            isDarkMode = isDarkMode,
            onToggle = {
                isDarkMode = !isDarkMode
            },
            modifier = Modifier.padding(16.dp)
        )
    }
}
