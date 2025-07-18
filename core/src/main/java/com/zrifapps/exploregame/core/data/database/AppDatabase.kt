package com.zrifapps.exploregame.core.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.zrifapps.exploregame.core.data.database.dao.FavouriteGameDao
import com.zrifapps.exploregame.core.data.database.entity.FavouriteGameEntity

@Database(
    entities = [FavouriteGameEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favouriteGameDao(): FavouriteGameDao

    companion object {
        const val DATABASE_NAME = "explore_game_database"
    }
}