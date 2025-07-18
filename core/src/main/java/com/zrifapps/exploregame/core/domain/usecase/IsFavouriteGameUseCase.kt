package com.zrifapps.exploregame.core.domain.usecase

import com.zrifapps.exploregame.core.domain.repository.FavouriteGameRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class IsFavouriteGameUseCase @Inject constructor(
    private val repository: FavouriteGameRepository
) {
    operator fun invoke(gameId: Int): Flow<Boolean> {
        return repository.isFavourite(gameId)
    }
}