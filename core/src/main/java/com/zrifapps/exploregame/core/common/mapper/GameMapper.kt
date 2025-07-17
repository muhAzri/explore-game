package com.zrifapps.exploregame.core.common.mapper

import com.zrifapps.exploregame.core.data.dto.GameDTO
import com.zrifapps.exploregame.core.domain.entity.Game


fun GameDTO.toDomain(): Game {
    return Game(
        id = id,
        name = name,
        slug = slug,
        released = released,
        backgroundImage = backgroundImage,
        rating = rating,
        ratingTop = ratingTop,
        ratings = ratings?.map { it.toDomain() },
        platforms = platforms?.map { it.toDomain() },
        genres = genres?.map { it.toDomain() },
        stores = stores?.map { it.toDomain() }
    )
}

fun List<GameDTO>.toDomain(): List<Game> {
    return map { it.toDomain() }
}
