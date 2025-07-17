package com.zrifapps.exploregame.core.common.mapper

import com.zrifapps.exploregame.core.data.dto.RatingDTO
import com.zrifapps.exploregame.core.domain.entity.Rating

fun RatingDTO.toDomain(): Rating {
    return Rating(
        id = id,
        title = title,
        count = count,
        percent = percent
    )
}
