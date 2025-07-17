package com.zrifapps.exploregame.game.domain.model


data class GetGamesRequest(
    val page: Int = 1,
    val pageSize: Int = 10,
    val search: String? = null,
)
