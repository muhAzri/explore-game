package com.zrifapps.exploregame.core.data.repository

import com.zrifapps.exploregame.core.network.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import retrofit2.HttpException
import java.io.IOException

abstract class BaseRepository {

    protected suspend fun <T> safeApiCall(
        apiCall: suspend () -> T,
    ): NetworkResult<T> = withContext(Dispatchers.IO) {
        try {
            val result = apiCall.invoke()
            NetworkResult.Success(result)
        } catch (throwable: Throwable) {
            when (throwable) {
                is HttpException -> {
                    val errorMessage = try {
                        val errorJson = throwable.response()?.errorBody()?.string()
                        JSONObject(errorJson ?: "").optString("message", throwable.message())
                    } catch (_: Exception) {
                        throwable.message()
                    }

                    NetworkResult.Error(
                        statusCode = throwable.code(),
                        message = errorMessage ?: "Http error"
                    )
                }

                is IOException -> {
                    NetworkResult.Error(
                        statusCode = -1,
                        message = "Network error, please check your connection."
                    )
                }

                else -> {
                    NetworkResult.Error(
                        statusCode = -1,
                        message = throwable.localizedMessage ?: "Unknown error occurred"
                    )
                }
            }
        }
    }
}
