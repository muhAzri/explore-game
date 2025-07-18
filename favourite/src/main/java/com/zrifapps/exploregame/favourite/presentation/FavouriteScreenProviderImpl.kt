package com.zrifapps.exploregame.favourite.presentation

import androidx.compose.runtime.Composable
import com.zrifapps.exploregame.core.presentation.FavouriteScreenProvider
import javax.inject.Inject

class FavouriteScreenProviderImpl @Inject constructor() : FavouriteScreenProvider {
    @Composable
    override fun FavouriteGamesScreen(
        onBackClick: () -> Unit,
        onGameClick: (Int) -> Unit
    ) {
        FavouriteGamesScreen(
            onBackClick = onBackClick,
            onGameClick = onGameClick
        )
    }
}
