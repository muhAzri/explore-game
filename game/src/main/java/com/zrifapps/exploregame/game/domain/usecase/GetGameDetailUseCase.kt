package com.zrifapps.exploregame.game.domain.usecase

import com.zrifapps.exploregame.core.domain.entity.Game
import com.zrifapps.exploregame.core.network.NetworkResult
import com.zrifapps.exploregame.game.data.repository.GameRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetGameDetailUseCase @Inject constructor(
    private val gameRepository: GameRepository,
) {
    operator fun invoke(gameId: String): Flow<NetworkResult<Game>> {
        return gameRepository.getGameDetail(gameId)
    }
}
