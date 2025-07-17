package com.zrifapps.exploregame.game.data.repository

import com.zrifapps.exploregame.core.domain.entity.Game
import com.zrifapps.exploregame.core.network.NetworkResult
import com.zrifapps.exploregame.game.domain.model.GetGamesRequest
import kotlinx.coroutines.flow.Flow


interface GameRepository {
    fun getGames(request: GetGamesRequest): Flow<NetworkResult<List<Game>>>
    fun getGameDetail(gameId: String): Flow<NetworkResult<Game>>
}
