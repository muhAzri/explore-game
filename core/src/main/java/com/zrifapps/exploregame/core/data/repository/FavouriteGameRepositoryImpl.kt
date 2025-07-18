package com.zrifapps.exploregame.core.data.repository

import com.zrifapps.exploregame.core.data.database.dao.FavouriteGameDao
import com.zrifapps.exploregame.core.data.database.entity.FavouriteGameEntity
import com.zrifapps.exploregame.core.domain.entity.Game
import com.zrifapps.exploregame.core.domain.entity.Genre
import com.zrifapps.exploregame.core.domain.entity.Platform
import com.zrifapps.exploregame.core.domain.entity.PlatformWrapper
import com.zrifapps.exploregame.core.domain.repository.FavouriteGameRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FavouriteGameRepositoryImpl @Inject constructor(
    private val favouriteGameDao: FavouriteGameDao
) : FavouriteGameRepository {

    override fun getAllFavouriteGames(): Flow<List<Game>> {
        return favouriteGameDao.getAllFavouriteGames().map { entities ->
            entities.map { entity ->
                Game(
                    id = entity.id,
                    name = entity.name,
                    slug = "",
                    backgroundImage = entity.backgroundImage,
                    rating = entity.rating,
                    ratingTop = 5,
                    released = entity.released,
                    ratings = null,
                    platforms = entity.platforms?.split(",")?.mapNotNull { platformName ->
                        if (platformName.isNotBlank()) {
                            PlatformWrapper(
                                platform = Platform(
                                    id = 0,
                                    name = platformName,
                                    slug = platformName.lowercase()
                                )
                            )
                        } else null
                    },
                    genres = entity.genres?.split(",")?.mapNotNull { genreName ->
                        if (genreName.isNotBlank()) {
                            Genre(
                                id = 0,
                                name = genreName,
                                slug = genreName.lowercase()
                            )
                        } else null
                    },
                    stores = null
                )
            }
        }
    }

    override fun isFavourite(gameId: Int): Flow<Boolean> {
        return favouriteGameDao.isFavourite(gameId)
    }

    override suspend fun addToFavourites(game: Game) {
        val entity = FavouriteGameEntity(
            id = game.id,
            name = game.name,
            backgroundImage = game.backgroundImage,
            rating = game.rating,
            released = game.released,
            platforms = game.platforms?.joinToString(",") { it.platform.name } ?: "",
            genres = game.genres?.joinToString(",") { it.name } ?: ""
        )
        favouriteGameDao.insertFavouriteGame(entity)
    }

    override suspend fun removeFromFavourites(gameId: Int) {
        favouriteGameDao.deleteFavouriteGameById(gameId)
    }

    override fun getFavouriteGamesCount(): Flow<Int> {
        return favouriteGameDao.getFavouriteGamesCount()
    }
}