package com.zrifapps.exploregame.game.domain.usecase

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.zrifapps.exploregame.core.domain.entity.Game
import com.zrifapps.exploregame.game.data.paging.GamePagingSource
import com.zrifapps.exploregame.game.data.repository.GameRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetGamesUseCase @Inject constructor(
    private val gameRepository: GameRepository,
) {
    operator fun invoke(search: String? = null): Flow<PagingData<Game>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false,
                initialLoadSize = 20
            ),
            pagingSourceFactory = {
                GamePagingSource(
                    gameRepository = gameRepository,
                    search = search
                )
            }
        ).flow
    }
}
