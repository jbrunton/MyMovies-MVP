package com.jbrunton.entities.models

sealed class LoadingState<out T> {
    data class Success<T>(val value: T): LoadingState<T>()
    data class Failure<T>(val cachedValue: T, val throwable: Throwable): LoadingState<T>()
    data class Loading<T>(val cachedValue: T): LoadingState<T>()
}
