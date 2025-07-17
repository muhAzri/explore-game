package com.zrifapps.exploregame.core.network

sealed class NetworkResult<out T> {
    data object Loading : NetworkResult<Nothing>()
    data class Success<T>(val data: T) : NetworkResult<T>()
    data class Error(
        val statusCode: Int,
        val message: String? = null,
    ) : NetworkResult<Nothing>()
}
