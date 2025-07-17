package com.zrifapps.exploregame.game.di


import com.zrifapps.exploregame.game.data.api.GameApi
import com.zrifapps.exploregame.game.data.repository.GameRepository
import com.zrifapps.exploregame.game.data.repository.GameRepositoryImpl
import com.zrifapps.exploregame.game.domain.usecase.GetGameDetailUseCase
import com.zrifapps.exploregame.game.domain.usecase.GetGamesUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.annotation.Signed
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object GameModule {

    @Provides
    @Singleton
    fun provideGameApi(retrofit: Retrofit): GameApi {
        return retrofit.create(GameApi::class.java)
    }

    @Provides
    @Singleton
    fun provideGameRepository(gameApi: GameApi): GameRepository {
        return GameRepositoryImpl(gameApi = gameApi)
    }

    @Provides
    @Singleton
    fun provideGetGameUseCase(gameRepository: GameRepository): GetGamesUseCase {
        return GetGamesUseCase(gameRepository = gameRepository)
    }

    @Provides
    @Signed
    fun provideGetGameDetailUseCase(gameRepository: GameRepository): GetGameDetailUseCase {
        return GetGameDetailUseCase(gameRepository = gameRepository)
    }
}
