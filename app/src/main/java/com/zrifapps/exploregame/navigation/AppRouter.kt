package com.zrifapps.exploregame.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.zrifapps.exploregame.features.DynamicModuleManager
import com.zrifapps.exploregame.game.presentation.screens.detail.GameDetailScreen
import com.zrifapps.exploregame.game.presentation.screens.list.GameListScreen
import com.zrifapps.exploregame.presentation.components.ModuleInstallDialog
import com.zrifapps.exploregame.presentation.screens.FavouriteScreenProxy

@Composable
fun AppRouter(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    startDestination: AppRoute,
) {
    val context = LocalContext.current
    val dynamicModuleManager = remember {
        DynamicModuleManager(context.applicationContext)
    }

    var showModuleInstallDialog by remember { mutableStateOf(false) }
    var moduleInstallState by remember {
        mutableStateOf<DynamicModuleManager.ModuleInstallState>(
            DynamicModuleManager.ModuleInstallState.NotInstalled
        )
    }
    var pendingNavigation by remember { mutableStateOf<AppRoute?>(null) }

    fun navigateTo(route: AppRoute, clearBackStack: Boolean = false) {
        if (route is FavoriteGames) {
            val isModuleInstalled = dynamicModuleManager.isModuleInstalled("favourite")

            if (isModuleInstalled) {
                navController.navigate(route) {
                    if (clearBackStack) {
                        popUpTo(navController.graph.startDestinationRoute ?: "") {
                            inclusive = true
                        }
                    }
                }
            } else {
                pendingNavigation = route
                showModuleInstallDialog = true
            }
        } else {
            navController.navigate(route) {
                if (clearBackStack) {
                    popUpTo(navController.graph.startDestinationRoute ?: "") { inclusive = true }
                }
            }
        }
    }

    LaunchedEffect(showModuleInstallDialog) {
        if (showModuleInstallDialog && pendingNavigation is FavoriteGames) {
            dynamicModuleManager.installModule("favourite").collect { state ->
                moduleInstallState = state
                if (state is DynamicModuleManager.ModuleInstallState.Installed) {
                    showModuleInstallDialog = false
                    pendingNavigation?.let { pendingRoute ->
                        navController.navigate(pendingRoute) {
                            popUpTo(navController.graph.startDestinationRoute ?: "") {
                                inclusive = true
                            }
                        }
                        pendingNavigation = null
                    }
                }
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable<GameList> {
            GameListScreen(
                onGameClicked = { gameId ->
                    navigateTo(GameDetail(gameId))
                },
                onFavoritesClicked = {
                    navigateTo(FavoriteGames)
                }
            )
        }

        composable<GameDetail> { backStackEntry ->
            val gameId = backStackEntry.arguments?.getString("gameId") ?: return@composable
            GameDetailScreen(
                gameId = gameId,
                onBackPressed = { navController.popBackStack() }
            )
        }

        composable<FavoriteGames> {
            FavouriteScreenProxy(
                onBackClick = {
                    navController.popBackStack()
                },
                onGameClick = { gameId ->
                    navigateTo(GameDetail(gameId.toString()))
                }
            )
        }
    }

    if (showModuleInstallDialog) {
        ModuleInstallDialog(
            moduleName = "Favourites",
            installState = moduleInstallState,
            onDismiss = {
                showModuleInstallDialog = false
                pendingNavigation = null
            },
            onRetry = {
                pendingNavigation?.let { route ->
                    navigateTo(route)
                }
            }
        )
    }
}
