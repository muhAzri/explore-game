package com.zrifapps.exploregame.core.domain.usecase

import com.zrifapps.exploregame.core.domain.entity.Game
import com.zrifapps.exploregame.core.domain.repository.FavouriteGameRepository
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class AddToFavouritesUseCaseTest {

    @Mock
    private lateinit var repository: FavouriteGameRepository

    private lateinit var useCase: AddToFavouritesUseCase
    
    companion object {
        private const val SIMPLE_GAME_ID = 1
        private const val COMPLEX_GAME_ID = 123
        private const val SIMPLE_GAME_NAME = "Test Game"
        private const val COMPLEX_GAME_NAME = "Complex Game"
        private const val TEST_RATING = 4.5
        private const val COMPLEX_RATING = 4.8
        private const val TEST_RATING_TOP = 5
        private const val TEST_DATE = "2023-01-01"
        private const val COMPLEX_DATE = "2023-12-01"
        private const val BASE_IMAGE_URL = "https://example.com/image"
        private const val ERROR_MESSAGE = "Database error"
    }

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        useCase = AddToFavouritesUseCase(repository)
    }

    @Test
    fun `invoke should call repository addToFavourites with correct game`() = runTest {
        // Given
        val game = createMockGame(SIMPLE_GAME_ID, SIMPLE_GAME_NAME)

        // When
        useCase(game)

        // Then
        verify(repository).addToFavourites(game)
    }

    @Test
    fun `invoke should handle repository exception`() = runTest {
        // Given
        val game = createMockGame(SIMPLE_GAME_ID, SIMPLE_GAME_NAME)
        val exception = RuntimeException(ERROR_MESSAGE)
        whenever(repository.addToFavourites(game)).thenThrow(exception)

        // When & Then
        try {
            useCase(game)
            fail("Expected exception to be thrown")
        } catch (e: RuntimeException) {
            assertEquals(ERROR_MESSAGE, e.message)
        }
        verify(repository).addToFavourites(game)
    }

    @Test
    fun `invoke should work with game having all fields populated`() = runTest {
        // Given
        val game = Game(
            id = COMPLEX_GAME_ID,
            name = COMPLEX_GAME_NAME,
            slug = "complex-game",
            backgroundImage = "$BASE_IMAGE_URL.jpg",
            rating = COMPLEX_RATING,
            ratingTop = TEST_RATING_TOP,
            ratings = emptyList(),
            genres = emptyList(),
            platforms = emptyList(),
            stores = emptyList(),
            released = COMPLEX_DATE
        )

        // When
        useCase(game)

        // Then
        verify(repository).addToFavourites(game)
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