package com.jbrunton.entities.models

import io.reactivex.Observable

sealed class LoadingState<out T> {
    data class Success<T>(val value: T): LoadingState<T>()
    data class Failure<T>(val throwable: Throwable, val cachedValue: T?): LoadingState<T>()
    data class Loading<T>(val cachedValue: T?): LoadingState<T>()
}

typealias DataStream<T> = Observable<LoadingState<T>>