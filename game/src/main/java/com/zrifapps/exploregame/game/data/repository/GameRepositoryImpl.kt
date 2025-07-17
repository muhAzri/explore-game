package com.zrifapps.exploregame.game.data.repository

import com.zrifapps.exploregame.core.common.mapper.toDomain
import com.zrifapps.exploregame.core.data.repository.BaseRepository
import com.zrifapps.exploregame.core.domain.entity.Game
import com.zrifapps.exploregame.core.network.NetworkResult
import com.zrifapps.exploregame.game.data.api.GameApi
import com.zrifapps.exploregame.game.domain.model.GetGamesRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


class GameRepositoryImpl @Inject constructor(
    private val gameApi: GameApi,
) : GameRepository, BaseRepository() {

    override fun getGames(request: GetGamesRequest): Flow<NetworkResult<List<Game>>> = flow {
        val result = safeApiCall {
            val response = gameApi.getGames(
                page = request.page,
                pageSize = request.pageSize,
                search = request.search
            )
            response.results.toDomain()
        }
        emit(result)
    }.catch { exception ->
        emit(
            NetworkResult.Error(
                statusCode = 0,
                message = exception.message
            )
        )
    }

    override fun getGameDetail(gameId: String): Flow<NetworkResult<Game>> = flow {
        val result = safeApiCall {
            val response = gameApi.getGameDetail(gameId)
            response.toDomain()
        }
        emit(result)
    }.catch { exception ->
        emit(
            NetworkResult.Error(
                statusCode = 0,
                message = exception.message
            )
        )
    }
}
