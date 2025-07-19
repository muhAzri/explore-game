package com.zrifapps.exploregame.core.domain.usecase

import com.zrifapps.exploregame.core.domain.entity.Game
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

class GetFavouriteGamesUseCaseTest {

    @Mock
    private lateinit var repository: FavouriteGameRepository

    private lateinit var useCase: GetFavouriteGamesUseCase
    
    companion object {
        private const val GAME_ID_1 = 1
        private const val GAME_ID_2 = 2
        private const val GAME_NAME_1 = "Game 1"
        private const val GAME_NAME_2 = "Game 2"
        private const val TEST_RATING = 4.5
        private const val TEST_RATING_TOP = 5
        private const val TEST_DATE = "2023-01-01"
        private const val BASE_IMAGE_URL = "https://example.com/image"
        private const val ERROR_MESSAGE = "Database error"
    }

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        useCase = GetFavouriteGamesUseCase(repository)
    }

    @Test
    fun `invoke should return games list when repository returns data`() = runTest {
        // Given
        val expectedGames = listOf(
            createMockGame(GAME_ID_1, GAME_NAME_1),
            createMockGame(GAME_ID_2, GAME_NAME_2)
        )
        whenever(repository.getAllFavouriteGames()).thenReturn(flowOf(expectedGames))

        // When
        val result = useCase().toList()

        // Then
        assertEquals(1, result.size)
        assertEquals(expectedGames, result[0])
        verify(repository).getAllFavouriteGames()
    }

    @Test
    fun `invoke should return empty list when repository returns empty data`() = runTest {
        // Given
        val expectedGames = emptyList<Game>()
        whenever(repository.getAllFavouriteGames()).thenReturn(flowOf(expectedGames))

        // When
        val result = useCase().toList()

        // Then
        assertEquals(1, result.size)
        assertEquals(expectedGames, result[0])
        verify(repository).getAllFavouriteGames()
    }

    @Test
    fun `invoke should handle repository exception`() = runTest {
        // Given
        val exception = RuntimeException(ERROR_MESSAGE)
        whenever(repository.getAllFavouriteGames()).thenThrow(exception)

        // When & Then
        try {
            useCase().toList()
            fail("Expected exception to be thrown")
        } catch (e: RuntimeException) {
            assertEquals(ERROR_MESSAGE, e.message)
        }
        verify(repository).getAllFavouriteGames()
    }

    private fun createMockGame(id: Int, name: String) = Game(
        id = id,
        name = name,
        slug = "game-$id",
        backgroundImage = "$BASE_IMAGE_URL$id.jpg",
        rating = TEST_RATING,
        ratingTop = TEST_RATING_TOP,
        ratings = emptyList(),
        genres = emptyList(),
        platforms = emptyList(),
        stores = emptyList(),
        released = TEST_DATE
    )
}