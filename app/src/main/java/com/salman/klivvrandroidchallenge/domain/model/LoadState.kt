package com.salman.klivvrandroidchallenge.domain.model

sealed class LoadState<out T> {
    data object Loading : LoadState<Nothing>()
    data object Idle : LoadState<Nothing>()
    class Success<out T>(val data: T, val code: Int = 200) : LoadState<T>()
    class Error(val message: String, val throwable: Throwable?) : LoadState<Nothing>()

    fun dataOrNull(): T? {
        return if (this is Success) {
            data
        } else {
            null
        }
    }

    fun isLoading(): Boolean = this is Loading
    fun isSuccess(): Boolean = this is Success
    fun isError(): Boolean = this is Error
    fun isIdle(): Boolean = this is Idle

    inline fun onIdle(action: () -> Unit): LoadState<T> {
        if (this is Idle) {
            action()
        }
        return this
    }

    inline fun onData(action: (data: T) -> Unit): LoadState<T> {
        if (this is Success) {
            action(data)
        }
        return this
    }

    inline fun onSuccess(action: (data: T, code: Int) -> Unit): LoadState<T> {
        if (this is Success) {
            action(data, code)
        }
        return this
    }

    inline fun onError(action: (message: String) -> Unit): LoadState<T> {
        if (this is Error) {
            action(message)
        }
        return this
    }

    inline fun onError(action: (message: String, throwable: Throwable?) -> Unit): LoadState<T> {
        if (this is Error) {
            action(message, throwable)
        }
        return this
    }

    inline fun onLoading(action: () -> Unit): LoadState<T> {
        if (this is Loading) {
            action()
        }
        return this
    }
}