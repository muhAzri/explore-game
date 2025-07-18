package com.zrifapps.exploregame.favourite.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zrifapps.exploregame.core.domain.entity.Game
import com.zrifapps.exploregame.core.domain.usecase.GetFavouriteGamesUseCase
import com.zrifapps.exploregame.core.domain.usecase.RemoveFromFavouritesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class FavouriteGamesViewModel(
    private val getFavouriteGamesUseCase: GetFavouriteGamesUseCase,
    private val removeFromFavouritesUseCase: RemoveFromFavouritesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(FavouriteGamesUiState())
    val uiState: StateFlow<FavouriteGamesUiState> = _uiState.asStateFlow()

    init {
        loadFavouriteGames()
    }

    private fun loadFavouriteGames() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            getFavouriteGamesUseCase()
                .catch { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = error.message ?: "Unknown error occurred"
                    )
                }
                .collect { games ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        favouriteGames = games,
                        error = null
                    )
                }
        }
    }

    fun removeFromFavourites(gameId: Int) {
        viewModelScope.launch {
            try {
                removeFromFavouritesUseCase(gameId)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.message ?: "Failed to remove from favourites"
                )
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}

data class FavouriteGamesUiState(
    val isLoading: Boolean = false,
    val favouriteGames: List<Game> = emptyList(),
    val error: String? = null
)