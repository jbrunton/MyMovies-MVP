package com.jbrunton.entities.models

import io.reactivex.Observable

sealed class LoadingState<out T> {
    data class Success<T>(val value: T): LoadingState<T>()
    data class Failure<T>(val error: Throwable, val cachedValue: T? = null): LoadingState<T>()
    data class Loading<T>(val cachedValue: T? = null): LoadingState<T>()
}

typealias DataStream<T> = Observable<LoadingState<T>>

fun <S, T>LoadingState<S>.map(transform: (S) -> T): LoadingState<T> {
    return when (this) {
        is LoadingState.Success -> LoadingState.Success(transform(this.value))
        is LoadingState.Loading -> LoadingState.Loading(this.cachedValue?.let(transform))
        is LoadingState.Failure -> LoadingState.Failure(this.error, this.cachedValue?.let(transform))
    }
}

fun <T>LoadingState<T>.onSuccess(transform: (LoadingState.Success<T>) -> LoadingState<T>): LoadingState<T> {
    return when (this) {
        is LoadingState.Success -> transform(this)
        else -> this
    }
}

fun <T>LoadingState<T>.onLoading(transform: (LoadingState.Loading<T>) -> LoadingState<T>): LoadingState<T> {
    return when (this) {
        is LoadingState.Loading -> transform(this)
        else -> this
    }
}

fun <T>LoadingState<T>.onFailure(transform: (LoadingState.Failure<T>) -> LoadingState<T>): LoadingState<T> {
    return when (this) {
        is LoadingState.Failure -> transform(this)
        else -> this
    }
}
