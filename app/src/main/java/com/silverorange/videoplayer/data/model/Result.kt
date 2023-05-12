package com.silverorange.videoplayer.data.model

sealed class Result<out T> {
    class Success<out T>(val data: T) : Result<T>()
    class Error<out T>(val code: Int, val message: String?) : Result<T>()
    class Failure(val e: Throwable) : Result<Nothing>()
}