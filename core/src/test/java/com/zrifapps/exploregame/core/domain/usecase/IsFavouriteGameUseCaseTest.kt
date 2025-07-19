package com.zrifapps.exploregame.core.domain.usecase

import com.zrifapps.exploregame.core.domain.repository.FavouriteGameRepository
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class IsFavouriteGameUseCaseTest {

    @Mock
    private lateinit var repository: FavouriteGameRepository

    private lateinit var useCase: IsFavouriteGameUseCase
    
    companion object {
        private const val FIRST_GAME_ID = 123
        private const val SECOND_GAME_ID = 456
        private const val THIRD_GAME_ID = 789
        private const val GAME_ID_1 = 1
        private const val GAME_ID_2 = 2
        private const val ERROR_MESSAGE = "Database error"
    }

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        useCase = IsFavouriteGameUseCase(repository)
    }

    @Test
    fun `invoke should return true when game is favourite`() = runTest {
        // Given
        val gameId = FIRST_GAME_ID
        whenever(repository.isFavourite(gameId)).thenReturn(flowOf(true))

        // When
        val result = useCase(gameId).toList()

        // Then
        assertEquals(1, result.size)
        assertTrue(result[0])
        verify(repository).isFavourite(gameId)
    }

    @Test
    fun `invoke should return false when game is not favourite`() = runTest {
        // Given
        val gameId = SECOND_GAME_ID
        whenever(repository.isFavourite(gameId)).thenReturn(flowOf(false))

        // When
        val result = useCase(gameId).toList()

        // Then
        assertEquals(1, result.size)
        assertFalse(result[0])
        verify(repository).isFavourite(gameId)
    }

    @Test
    fun `invoke should handle repository exception`() = runTest {
        // Given
        val gameId = THIRD_GAME_ID
        val exception = RuntimeException(ERROR_MESSAGE)
        whenever(repository.isFavourite(gameId)).thenThrow(exception)

        // When & Then
        try {
            useCase(gameId).toList()
            fail("Expected exception to be thrown")
        } catch (e: RuntimeException) {
            assertEquals(ERROR_MESSAGE, e.message)
        }
        verify(repository).isFavourite(gameId)
    }

    @Test
    fun `invoke should work with different game IDs`() = runTest {
        // Given
        val gameId1 = GAME_ID_1
        val gameId2 = GAME_ID_2
        whenever(repository.isFavourite(gameId1)).thenReturn(flowOf(true))
        whenever(repository.isFavourite(gameId2)).thenReturn(flowOf(false))

        // When
        val result1 = useCase(gameId1).toList()
        val result2 = useCase(gameId2).toList()

        // Then
        assertTrue(result1[0])
        assertFalse(result2[0])
        verify(repository).isFavourite(gameId1)
        verify(repository).isFavourite(gameId2)
    }
}