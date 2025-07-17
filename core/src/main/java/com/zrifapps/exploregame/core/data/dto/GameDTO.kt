package com.zrifapps.exploregame.core.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GameDTO(
    val id: Int,
    val name: String,
    val slug: String,
    val released: String?,
    @Json(name = "background_image") val backgroundImage: String?,
    val rating: Double,
    @Json(name = "rating_top") val ratingTop: Int,
    val ratings: List<RatingDTO>?,
    val platforms: List<PlatformWrapperDTO>?,
    val genres: List<GenreDTO>?,
    val stores: List<StoreWrapperDTO>?,
)

