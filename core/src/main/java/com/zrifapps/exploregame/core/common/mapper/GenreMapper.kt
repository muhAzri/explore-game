package com.zrifapps.exploregame.core.common.mapper

import com.zrifapps.exploregame.core.data.dto.GenreDTO
import com.zrifapps.exploregame.core.domain.entity.Genre

fun GenreDTO.toDomain(): Genre {
    return Genre(
        id = id,
        name = name,
        slug = slug
    )
}
