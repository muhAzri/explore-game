package com.zrifapps.exploregame.core.common.mapper

import com.zrifapps.exploregame.core.data.dto.StoreWrapperDTO
import com.zrifapps.exploregame.core.domain.entity.StoreWrapper

fun StoreWrapperDTO.toDomain(): StoreWrapper {
    return StoreWrapper(
        store = store.toDomain()
    )
}
