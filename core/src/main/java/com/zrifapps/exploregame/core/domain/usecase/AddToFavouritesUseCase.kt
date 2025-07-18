package com.zrifapps.exploregame.core.domain.usecase

import com.zrifapps.exploregame.core.domain.entity.Game
import com.zrifapps.exploregame.core.domain.repository.FavouriteGameRepository
import javax.inject.Inject

class AddToFavouritesUseCase @Inject constructor(
    private val repository: FavouriteGameRepository
) {
    suspend operator fun invoke(game: Game) {
        repository.addToFavourites(game)
    }
}