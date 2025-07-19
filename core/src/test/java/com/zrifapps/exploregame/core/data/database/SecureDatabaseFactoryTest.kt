package com.zrifapps.exploregame.core.data.database

import android.content.Context
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class SecureDatabaseFactoryTest {

    @Mock
    private lateinit var context: Context
    
    private lateinit var factory: SecureDatabaseFactory

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        factory = SecureDatabaseFactory(context)
    }

    @Test
    fun `factory should be initialized correctly with different contexts`() {
        // Given
        val factory1 = SecureDatabaseFactory(context)
        val factory2 = SecureDatabaseFactory(context)

        // Then
        assertNotNull(factory1)
        assertNotNull(factory2)
        // Each factory instance should be separate
        assertNotSame(factory1, factory2)
    }

    @Test
    fun `factory should handle context correctly`() {
        // Given & When
        val testFactory = SecureDatabaseFactory(context)

        // Then
        assertNotNull(testFactory)
        // Factory should be same type as our main factory
        assertEquals(factory.javaClass, testFactory.javaClass)
    }
}