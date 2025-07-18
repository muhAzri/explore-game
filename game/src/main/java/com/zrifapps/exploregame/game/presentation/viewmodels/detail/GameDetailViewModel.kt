package com.zrifapps.exploregame.game.presentation.viewmodels.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zrifapps.exploregame.core.domain.entity.Game
import com.zrifapps.exploregame.core.domain.usecase.AddToFavouritesUseCase
import com.zrifapps.exploregame.core.domain.usecase.IsFavouriteGameUseCase
import com.zrifapps.exploregame.core.domain.usecase.RemoveFromFavouritesUseCase
import com.zrifapps.exploregame.core.network.NetworkResult
import com.zrifapps.exploregame.game.domain.usecase.GetGameDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameDetailViewModel @Inject constructor(
    private val getGameDetailUseCase: GetGameDetailUseCase,
    private val isFavouriteGameUseCase: IsFavouriteGameUseCase,
    private val addToFavouritesUseCase: AddToFavouritesUseCase,
    private val removeFromFavouritesUseCase: RemoveFromFavouritesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(GameDetailUiState())
    val uiState: StateFlow<GameDetailUiState> = _uiState.asStateFlow()

    fun getGameDetail(gameId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            getGameDetailUseCase(gameId).collect { result ->
                when (result) {
                    is NetworkResult.Success -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            game = result.data,
                            error = null
                        )

                        result.data?.let { game ->
                            checkFavouriteStatus(game.id)
                        }
                    }

                    is NetworkResult.Error -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = result.message ?: "Unknown error occurred"
                        )
                    }

                    is NetworkResult.Loading -> {
                        _uiState.value = _uiState.value.copy(isLoading = true)
                    }
                }
            }
        }
    }

    private fun checkFavouriteStatus(gameId: Int) {
        viewModelScope.launch {
            isFavouriteGameUseCase(gameId).collect { isFavourite ->
                _uiState.value = _uiState.value.copy(isFavourite = isFavourite)
            }
        }
    }

    fun toggleFavourite() {
        val currentGame = _uiState.value.game ?: return
        val isFavourite = _uiState.value.isFavourite

        viewModelScope.launch {
            try {
                if (isFavourite) {
                    removeFromFavouritesUseCase(currentGame.id)
                } else {
                    addToFavouritesUseCase(currentGame)
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.message ?: "Failed to update favourites"
                )
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    fun retry(gameId: String) {
        getGameDetail(gameId)
    }
}

data class GameDetailUiState(
    val isLoading: Boolean = false,
    val game: Game? = null,
    val error: String? = null,
    val isFavourite: Boolean = false
)
