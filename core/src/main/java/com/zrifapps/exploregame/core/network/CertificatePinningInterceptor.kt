package com.zrifapps.exploregame.core.network

import android.content.Context
import okhttp3.CertificatePinner
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import java.io.IOException
import java.io.InputStream
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate
import java.security.MessageDigest
import android.util.Base64
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CertificatePinningInterceptor @Inject constructor(
    private val context: Context
) : Interceptor {

    companion object {
        private const val API_HOST = "api.rawg.io"
        private const val CERTIFICATE_FILE = "rawgio.cer"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        return chain.proceed(chain.request())
    }

    fun createSecureOkHttpClient(baseClient: OkHttpClient): OkHttpClient {
        val certificatePinner = createCertificatePinner()
        
        return baseClient.newBuilder()
            .certificatePinner(certificatePinner)
            .build()
    }

    private fun createCertificatePinner(): CertificatePinner {
        return try {
            val pin = extractPinFromCertificate()
            CertificatePinner.Builder()
                .add(API_HOST, pin)
                .build()
        } catch (e: Exception) {
            // If certificate pinning fails, create with actual RAWG.io pins
            // These are the actual RAWG.io certificate pins (SHA256)
            CertificatePinner.Builder()
                .add(API_HOST, "sha256/5KX4+KsKzPZ5DKq+bSzMIKtNhtc2KNpcMWsF6Hi+I2g=") // RAWG.io certificate
                .add(API_HOST, "sha256/jQJTbIh0grw0/1TkHSumWb+Fs0Ggogr621gT3PvPKG0=") // Let's Encrypt R3
                .add(API_HOST, "sha256/C5+lpZ7tcVwmwQIMcRtPbsQtWLABXhQzejna0wHFr8M=") // Let's Encrypt Root
                .build()
        }
    }

    private fun extractPinFromCertificate(): String {
        val inputStream: InputStream = context.assets.open(CERTIFICATE_FILE)
        val certificateFactory = CertificateFactory.getInstance("X.509")
        val certificate = certificateFactory.generateCertificate(inputStream) as X509Certificate

        val digest = MessageDigest.getInstance("SHA-256")
        val publicKeyBytes = certificate.publicKey.encoded
        val hash = digest.digest(publicKeyBytes)

        return "sha256/" + Base64.encodeToString(hash, Base64.NO_WRAP)
    }
}