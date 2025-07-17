package com.zrifapps.exploregame.game.data.api

import com.zrifapps.exploregame.core.data.dto.GameDTO
import com.zrifapps.exploregame.core.data.response.BaseResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GameApi {
    @GET("games")
    suspend fun getGames(
        @Query("page") page: Int,
        @Query("page_size") pageSize: Int,
        @Query("search") search: String?,
    ): BaseResponse<List<GameDTO>>

    @GET("games/{id}")
    suspend fun getGameDetail(
        @Path("id") gameId: String,
    ): GameDTO
}
