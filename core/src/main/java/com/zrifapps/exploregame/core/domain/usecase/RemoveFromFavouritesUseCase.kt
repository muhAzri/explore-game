package com.zrifapps.exploregame.core.domain.usecase

import com.zrifapps.exploregame.core.domain.repository.FavouriteGameRepository
import javax.inject.Inject

class RemoveFromFavouritesUseCase @Inject constructor(
    private val repository: FavouriteGameRepository
) {
    suspend operator fun invoke(gameId: Int) {
        repository.removeFromFavourites(gameId)
    }
}