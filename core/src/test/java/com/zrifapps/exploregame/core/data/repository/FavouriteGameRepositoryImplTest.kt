package com.zrifapps.exploregame.core.data.repository

import com.zrifapps.exploregame.core.data.database.dao.FavouriteGameDao
import com.zrifapps.exploregame.core.data.database.entity.FavouriteGameEntity
import com.zrifapps.exploregame.core.domain.entity.Game
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

class FavouriteGameRepositoryImplTest {

    @Mock
    private lateinit var dao: FavouriteGameDao

    private lateinit var repository: FavouriteGameRepositoryImpl
    
    companion object {
        private const val GAME_ID_1 = 1
        private const val GAME_ID_2 = 2
        private const val TEST_GAME_ID = 123
        private const val ANOTHER_GAME_ID = 456
        private const val THIRD_GAME_ID = 789
        private const val GAME_NAME_1 = "Game 1"
        private const val GAME_NAME_2 = "Game 2"
        private const val TEST_GAME_NAME = "Test Game"
        private const val TEST_RATING = 4.5
        private const val TEST_RATING_TOP = 5
        private const val TEST_DATE = "2023-01-01"
        private const val TEST_PLATFORMS = "PC,PlayStation"
        private const val TEST_GENRES = "Action,Adventure"
        private const val BASE_IMAGE_URL = "https://example.com/image"
    }

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        repository = FavouriteGameRepositoryImpl(dao)
    }

    @Test
    fun `getAllFavouriteGames should return mapped games from dao`() = runTest {
        // Given
        val entities = listOf(
            createMockEntity(GAME_ID_1, GAME_NAME_1),
            createMockEntity(GAME_ID_2, GAME_NAME_2)
        )
        whenever(dao.getAllFavouriteGames()).thenReturn(flowOf(entities))

        // When
        val result = repository.getAllFavouriteGames().toList()

        // Then
        assertEquals(1, result.size)
        assertEquals(2, result[0].size)
        verify(dao).getAllFavouriteGames()
    }

    @Test
    fun `getAllFavouriteGames should return empty list when dao returns empty list`() = runTest {
        // Given
        whenever(dao.getAllFavouriteGames()).thenReturn(flowOf(emptyList()))

        // When
        val result = repository.getAllFavouriteGames().toList()

        // Then
        assertEquals(1, result.size)
        assertTrue(result[0].isEmpty())
        verify(dao).getAllFavouriteGames()
    }

    @Test
    fun `addToFavourites should call dao insertFavouriteGame`() = runTest {
        // Given
        val game = createMockGame(GAME_ID_1, TEST_GAME_NAME)

        // When
        repository.addToFavourites(game)

        // Then
        verify(dao).insertFavouriteGame(org.mockito.kotlin.any())
    }

    @Test
    fun `removeFromFavourites should call dao deleteFavouriteGameById with correct ID`() = runTest {
        // Given
        val gameId = TEST_GAME_ID

        // When
        repository.removeFromFavourites(gameId)

        // Then
        verify(dao).deleteFavouriteGameById(gameId)
    }

    @Test
    fun `isFavourite should return true when dao returns true`() = runTest {
        // Given
        val gameId = ANOTHER_GAME_ID
        whenever(dao.isFavourite(gameId)).thenReturn(flowOf(true))

        // When
        val result = repository.isFavourite(gameId).toList()

        // Then
        assertEquals(1, result.size)
        assertTrue(result[0])
        verify(dao).isFavourite(gameId)
    }

    @Test
    fun `isFavourite should return false when dao returns false`() = runTest {
        // Given
        val gameId = THIRD_GAME_ID
        whenever(dao.isFavourite(gameId)).thenReturn(flowOf(false))

        // When
        val result = repository.isFavourite(gameId).toList()

        // Then
        assertEquals(1, result.size)
        assertFalse(result[0])
        verify(dao).isFavourite(gameId)
    }

    private fun createMockEntity(id: Int, name: String) = FavouriteGameEntity(
        id = id,
        name = name,
        backgroundImage = "$BASE_IMAGE_URL$id.jpg",
        rating = TEST_RATING,
        released = TEST_DATE,
        platforms = TEST_PLATFORMS,
        genres = TEST_GENRES
    )

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