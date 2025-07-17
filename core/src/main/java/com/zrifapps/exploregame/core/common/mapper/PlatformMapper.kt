package com.zrifapps.exploregame.core.common.mapper

import com.zrifapps.exploregame.core.data.dto.PlatformDTO
import com.zrifapps.exploregame.core.domain.entity.Platform

fun PlatformDTO.toDomain(): Platform {
    return Platform(
        id = id,
        name = name,
        slug = slug
    )
}
