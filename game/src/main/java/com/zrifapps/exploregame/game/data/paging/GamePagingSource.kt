package com.zrifapps.exploregame.game.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.zrifapps.exploregame.core.domain.entity.Game
import com.zrifapps.exploregame.core.network.NetworkResult
import com.zrifapps.exploregame.game.data.repository.GameRepository
import com.zrifapps.exploregame.game.domain.model.GetGamesRequest
import kotlinx.coroutines.flow.first
import javax.inject.Inject


class GamePagingSource @Inject constructor(
    private val gameRepository: GameRepository,
    private val search: String?,
) : PagingSource<Int, Game>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Game> {
        return try {
            val page = params.key ?: 1
            val pageSize = params.loadSize

            val result = gameRepository.getGames(
                request = GetGamesRequest(
                    page = page,
                    pageSize = pageSize,
                    search = search
                ),
            ).first()

            when (result) {
                is NetworkResult.Success -> {
                    val games = result.data
                    LoadResult.Page(
                        data = games,
                        prevKey = if (page == 1) null else page - 1,
                        nextKey = if (games.isEmpty()) null else page + 1
                    )
                }

                is NetworkResult.Error -> LoadResult.Error(
                    Exception(result.message)
                )

                is NetworkResult.Loading -> LoadResult.Error(
                    Exception("Unexpected loading state")
                )
            }
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Game>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}
