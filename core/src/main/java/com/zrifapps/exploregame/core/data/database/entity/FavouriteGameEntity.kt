package com.zrifapps.exploregame.core.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favourite_games")
data class FavouriteGameEntity(
    @PrimaryKey
    val id: Int,
    val name: String,
    val backgroundImage: String?,
    val rating: Double,
    val released: String?,
    val platforms: String?,
    val genres: String?,
    val dateAdded: Long = System.currentTimeMillis()
)