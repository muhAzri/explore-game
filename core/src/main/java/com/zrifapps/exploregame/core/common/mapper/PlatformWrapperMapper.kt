package com.zrifapps.exploregame.core.common.mapper

import com.zrifapps.exploregame.core.data.dto.PlatformWrapperDTO
import com.zrifapps.exploregame.core.domain.entity.PlatformWrapper

fun PlatformWrapperDTO.toDomain(): PlatformWrapper {
    return PlatformWrapper(
        platform = platform.toDomain()
    )
}

