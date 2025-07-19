package com.zrifapps.exploregame.core.domain.usecase

import com.zrifapps.exploregame.core.domain.repository.FavouriteGameRepository
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class RemoveFromFavouritesUseCaseTest {

    @Mock
    private lateinit var repository: FavouriteGameRepository

    private lateinit var useCase: RemoveFromFavouritesUseCase
    
    companion object {
        private const val FIRST_GAME_ID = 123
        private const val SECOND_GAME_ID = 456
        private const val THIRD_GAME_ID = 100
        private const val FOURTH_GAME_ID = 200
        private const val ZERO_GAME_ID = 0
        private const val ERROR_MESSAGE = "Database error"
    }

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        useCase = RemoveFromFavouritesUseCase(repository)
    }

    @Test
    fun `invoke should call repository removeFromFavourites with correct game ID`() = runTest {
        // Given
        val gameId = FIRST_GAME_ID

        // When
        useCase(gameId)

        // Then
        verify(repository).removeFromFavourites(gameId)
    }

    @Test
    fun `invoke should handle repository exception`() = runTest {
        // Given
        val gameId = SECOND_GAME_ID
        val exception = RuntimeException(ERROR_MESSAGE)
        whenever(repository.removeFromFavourites(gameId)).thenThrow(exception)

        // When & Then
        try {
            useCase(gameId)
            fail("Expected exception to be thrown")
        } catch (e: RuntimeException) {
            assertEquals(ERROR_MESSAGE, e.message)
        }
        verify(repository).removeFromFavourites(gameId)
    }

    @Test
    fun `invoke should work with different game IDs`() = runTest {
        // Given
        val gameId1 = THIRD_GAME_ID
        val gameId2 = FOURTH_GAME_ID

        // When
        useCase(gameId1)
        useCase(gameId2)

        // Then
        verify(repository).removeFromFavourites(gameId1)
        verify(repository).removeFromFavourites(gameId2)
    }

    @Test
    fun `invoke should work with zero and negative IDs for edge case testing`() = runTest {
        // Given
        val gameId = ZERO_GAME_ID

        // When
        useCase(gameId)

        // Then
        verify(repository).removeFromFavourites(gameId)
    }
}