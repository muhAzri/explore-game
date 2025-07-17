package com.zrifapps.exploregame.game.presentation.components.chip

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun PlatformChip(platformName: String) {
    val displayName = when {
        platformName.contains("PlayStation", ignoreCase = true) -> "PS"
        platformName.contains("Xbox", ignoreCase = true) -> "XB"
        platformName.contains("Nintendo", ignoreCase = true) -> "NS"
        platformName.contains("PC", ignoreCase = true) -> "PC"
        platformName.contains("Android", ignoreCase = true) -> "AND"
        platformName.contains("iOS", ignoreCase = true) -> "iOS"
        else -> platformName.take(2)
    }

    Surface(
        modifier = Modifier.height(20.dp),
        shape = RoundedCornerShape(4.dp),
        color = MaterialTheme.colorScheme.surfaceVariant
    ) {
        Text(
            text = displayName,
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
        )
    }
}

