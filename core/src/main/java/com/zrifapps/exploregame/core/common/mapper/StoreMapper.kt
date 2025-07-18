package com.zrifapps.exploregame.core.common.mapper

import com.zrifapps.exploregame.core.data.dto.StoreDTO
import com.zrifapps.exploregame.core.domain.entity.Store

fun StoreDTO.toDomain(): Store {
    return Store(
        id = id,
        name = name,
        slug = slug
    )
}
