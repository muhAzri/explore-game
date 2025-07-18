package com.zrifapps.exploregame.core.presentation

import androidx.compose.runtime.Composable

interface FavouriteScreenProvider {
    @Composable
    fun FavouriteGamesScreen(
        onBackClick: () -> Unit,
        onGameClick: (Int) -> Unit
    )
}