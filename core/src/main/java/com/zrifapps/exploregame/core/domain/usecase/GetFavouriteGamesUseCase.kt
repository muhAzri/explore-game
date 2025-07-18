package com.zrifapps.exploregame.core.domain.usecase

import com.zrifapps.exploregame.core.domain.entity.Game
import com.zrifapps.exploregame.core.domain.repository.FavouriteGameRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFavouriteGamesUseCase @Inject constructor(
    private val repository: FavouriteGameRepository
) {
    operator fun invoke(): Flow<List<Game>> {
        return repository.getAllFavouriteGames()
    }
}