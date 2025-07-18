package com.zrifapps.exploregame.core.domain.repository

import com.zrifapps.exploregame.core.domain.entity.Game
import kotlinx.coroutines.flow.Flow

interface FavouriteGameRepository {
    fun getAllFavouriteGames(): Flow<List<Game>>
    fun isFavourite(gameId: Int): Flow<Boolean>
    suspend fun addToFavourites(game: Game)
    suspend fun removeFromFavourites(gameId: Int)
    fun getFavouriteGamesCount(): Flow<Int>
}