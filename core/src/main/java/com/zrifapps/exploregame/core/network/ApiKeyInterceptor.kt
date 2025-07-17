package com.zrifapps.exploregame.core.network

import com.zrifapps.exploregame.core.common.manager.ConfigManager
import okhttp3.Interceptor
import okhttp3.Response

class ApiKeyInterceptor(private val configManager: ConfigManager) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val originalUrl = originalRequest.url

        val newUrl = originalUrl.newBuilder()
            .addQueryParameter("key", configManager.apiKey)
            .build()

        val newRequest = originalRequest.newBuilder()
            .url(newUrl)
            .build()


        return chain.proceed(newRequest)
    }
}
