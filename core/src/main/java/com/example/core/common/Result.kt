package com.example.core.common

sealed class Result<out T> {
    data object Loading : Result<Nothing>()

    data class Success<T>(val data: T) : Result<T>()

    data class Failure(
        val message: String,
        val exception: Throwable? = null
    ) : Result<Nothing>()


}
