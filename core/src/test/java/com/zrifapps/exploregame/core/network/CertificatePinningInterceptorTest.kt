package com.zrifapps.exploregame.core.network

import android.content.Context
import okhttp3.CertificatePinner
import okhttp3.OkHttpClient
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class CertificatePinningInterceptorTest {

    @Mock
    private lateinit var context: Context

    private lateinit var interceptor: CertificatePinningInterceptor
    
    companion object {
        private const val CONNECT_TIMEOUT_SECONDS = 30L
        private const val READ_TIMEOUT_SECONDS = 60L
    }

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        interceptor = CertificatePinningInterceptor(context)
    }

    @Test
    fun `interceptor should be initialized correctly`() {
        // Given & When
        val testInterceptor = CertificatePinningInterceptor(context)

        // Then
        assertNotNull(testInterceptor)
    }

    @Test
    fun `createSecureOkHttpClient should return OkHttpClient with certificate pinning`() {
        // Given
        val baseClient = OkHttpClient.Builder().build()

        // When
        val secureClient = interceptor.createSecureOkHttpClient(baseClient)

        // Then
        assertNotNull(secureClient)
        assertNotNull(secureClient.certificatePinner)
        // Verify the client has certificate pinning enabled
        assertFalse(secureClient.certificatePinner.pins.isEmpty())
    }

    @Test
    fun `createSecureOkHttpClient should preserve base client configuration`() {
        // Given
        val baseClient = OkHttpClient.Builder()
            .connectTimeout(CONNECT_TIMEOUT_SECONDS, java.util.concurrent.TimeUnit.SECONDS)
            .build()

        // When
        val secureClient = interceptor.createSecureOkHttpClient(baseClient)

        // Then
        assertNotNull(secureClient)
        assertEquals(baseClient.connectTimeoutMillis, secureClient.connectTimeoutMillis)
    }

    @Test
    fun `certificate pinner should be configured with pins`() {
        // Given
        val baseClient = OkHttpClient.Builder().build()

        // When
        val secureClient = interceptor.createSecureOkHttpClient(baseClient)

        // Then
        assertNotNull(secureClient.certificatePinner)
        assertFalse(secureClient.certificatePinner.pins.isEmpty())
    }

    @Test
    fun `interceptor should handle different base clients`() {
        // Given
        val baseClient1 = OkHttpClient.Builder().build()
        val baseClient2 = OkHttpClient.Builder()
            .readTimeout(READ_TIMEOUT_SECONDS, java.util.concurrent.TimeUnit.SECONDS)
            .build()

        // When
        val secureClient1 = interceptor.createSecureOkHttpClient(baseClient1)
        val secureClient2 = interceptor.createSecureOkHttpClient(baseClient2)

        // Then
        assertNotNull(secureClient1)
        assertNotNull(secureClient2)
        assertNotEquals(secureClient1.readTimeoutMillis, secureClient2.readTimeoutMillis)
    }
}