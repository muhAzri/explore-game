package com.zrifapps.exploregame.game.presentation.screens.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.zrifapps.exploregame.core.domain.entity.Game
import com.zrifapps.exploregame.game.presentation.components.search_bar.SearchBar
import com.zrifapps.exploregame.game.presentation.components.skeleton.GameListTileSkeleton
import com.zrifapps.exploregame.game.presentation.components.tile.GameListTile
import com.zrifapps.exploregame.game.presentation.viewmodels.list.GameListViewModel

@Composable
fun GameListScreen(
    onGameClicked: (String) -> Unit,
    onFavoritesClicked: () -> Unit,
    viewModel: GameListViewModel = hiltViewModel(),
) {
    val games = viewModel.games.collectAsLazyPagingItems()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            GameListTopBar(onFavoritesClicked = onFavoritesClicked)
        }
    ) { paddingValues ->
        GameListContent(
            games = games,
            searchQuery = searchQuery,
            onSearchQueryChanged = viewModel::setSearchQuery,
            onGameClicked = onGameClicked,
            modifier = Modifier.padding(paddingValues)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GameListTopBar(
    onFavoritesClicked: () -> Unit,
) {
    TopAppBar(
        title = { Text("Game Explorer") },
        actions = {
            IconButton(onClick = onFavoritesClicked) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Favorites"
                )
            }
        }
    )
}

@Composable
private fun GameListContent(
    games: LazyPagingItems<Game>,
    searchQuery: String,
    onSearchQueryChanged: (String) -> Unit,
    onGameClicked: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        SearchBar(
            searchQuery = searchQuery,
            onSearchQueryChanged = onSearchQueryChanged
        )

        GameList(
            games = games,
            onGameClicked = onGameClicked
        )
    }
}

@Composable
private fun GameList(
    games: LazyPagingItems<Game>,
    onGameClicked: (String) -> Unit,
) {
    when (val refreshState = games.loadState.refresh) {
        is LoadState.Loading -> {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(count = 10) {
                    GameListTileSkeleton()
                }
            }
        }

        is LoadState.Error -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                ErrorMessage(
                    message = refreshState.error.localizedMessage ?: "Unknown error"
                )
            }
        }

        else -> {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(count = games.itemCount) { index ->
                    val game = games[index]
                    game?.let {
                        GameListTile(
                            game = it,
                            onClick = { onGameClicked(it.id.toString()) }
                        )
                    }
                }

                when (val appendState = games.loadState.append) {
                    is LoadState.Loading -> {
                        items(count = 3) {
                            GameListTileSkeleton()
                        }
                    }

                    is LoadState.Error -> {
                        item {
                            ErrorMessage(
                                message = appendState.error.localizedMessage ?: "Unknown error",
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }

                    else -> {}
                }
            }
        }
    }
}

@Composable
private fun ErrorMessage(
    message: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "😕",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = "Oops! Something went wrong",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.error,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
