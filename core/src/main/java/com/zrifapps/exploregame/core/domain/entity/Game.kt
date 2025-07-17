package com.zrifapps.exploregame.core.domain.entity

data class Game(
    val id: Int,
    val name: String,
    val slug: String,
    val released: String?,
    val backgroundImage: String?,
    val rating: Double,
    val ratingTop: Int,
    val ratings: List<Rating>?,
    val platforms: List<PlatformWrapper>?,
    val genres: List<Genre>?,
    val stores: List<StoreWrapper>?,
)

