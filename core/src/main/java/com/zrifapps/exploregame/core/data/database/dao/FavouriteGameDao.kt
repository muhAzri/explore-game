package com.zrifapps.exploregame.core.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.zrifapps.exploregame.core.data.database.entity.FavouriteGameEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavouriteGameDao {
    @Query("SELECT * FROM favourite_games ORDER BY dateAdded DESC")
    fun getAllFavouriteGames(): Flow<List<FavouriteGameEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM favourite_games WHERE id = :gameId)")
    fun isFavourite(gameId: Int): Flow<Boolean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavouriteGame(game: FavouriteGameEntity)

    @Query("DELETE FROM favourite_games WHERE id = :gameId")
    suspend fun deleteFavouriteGameById(gameId: Int)

    @Query("SELECT COUNT(*) FROM favourite_games")
    fun getFavouriteGamesCount(): Flow<Int>
}