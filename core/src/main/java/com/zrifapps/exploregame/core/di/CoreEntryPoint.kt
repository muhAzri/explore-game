package com.zrifapps.exploregame.core.di

import com.zrifapps.exploregame.core.domain.usecase.AddToFavouritesUseCase
import com.zrifapps.exploregame.core.domain.usecase.GetFavouriteGamesUseCase
import com.zrifapps.exploregame.core.domain.usecase.IsFavouriteGameUseCase
import com.zrifapps.exploregame.core.domain.usecase.RemoveFromFavouritesUseCase
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface CoreEntryPoint {
    fun getFavouriteGamesUseCase(): GetFavouriteGamesUseCase
    fun isFavouriteGameUseCase(): IsFavouriteGameUseCase
    fun addToFavouritesUseCase(): AddToFavouritesUseCase
    fun removeFromFavouritesUseCase(): RemoveFromFavouritesUseCase
}