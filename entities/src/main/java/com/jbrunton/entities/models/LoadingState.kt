package com.jbrunton.entities.models

import io.reactivex.Observable
import kotlin.reflect.KClass

sealed class LoadingState<out T> {
    data class Success<T>(val value: T): LoadingState<T>()
    data class Failure<T>(val error: Throwable, val cachedValue: T? = null): LoadingState<T>()
    data class Loading<T>(val cachedValue: T? = null): LoadingState<T>()
}

typealias DataStream<T> = Observable<LoadingState<T>>

fun <T>LoadingState<T>.toValueOrNull(): T? {
    return when (this) {
        is LoadingState.Success -> this.value
        is LoadingState.Loading -> this.cachedValue
        is LoadingState.Failure -> this.cachedValue
    }
}

fun <T>LoadingState<T>.toValue(): T {
    return when (this) {
        is LoadingState.Success -> this.value
        is LoadingState.Loading -> this.cachedValue ?: throw NullPointerException()
        is LoadingState.Failure -> throw this.error
    }
}

fun <S, T, U>LoadingState<S>.zipWith(other: LoadingState<T>, transform: (S, T) -> U): LoadingState<U> {
    if (this is LoadingState.Success && other is LoadingState.Success) {
        return LoadingState.Success(transform(this.value, other.value))
    }

    val cachedValue = this.toValueOrNull()?.let { thisValue ->
        other.toValueOrNull()?.let { otherValue -> transform(thisValue, otherValue) }
    }

    return if (this is LoadingState.Failure) {
        LoadingState.Failure(this.error, cachedValue)
    } else if (other is LoadingState.Failure) {
        LoadingState.Failure(other.error, cachedValue)
    } else {
        LoadingState.Loading(cachedValue)
    }
}

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

inline fun <T, reified E: Throwable>LoadingState<T>.onError(predicate: (E) -> Boolean, errorHandler: (E) -> LoadingState<T>): LoadingState<T> {
    return when (this) {
        is LoadingState.Failure -> {
            if (this.error is E && predicate(this.error)) {
                errorHandler(this.error)
            } else {
                this
            }
        }
        else -> this
    }
}

fun <T, E: Throwable>LoadingState<T>.onError(
        klass: KClass<E>,
        block: ErrorHandler<T, E>.() -> Unit
) = ErrorHandler(klass, this).apply(block).handle()

class ErrorHandler<T, E: Throwable>(val klass: KClass<E>, val state: LoadingState<T>) {
    var filter: (E) -> Boolean = { true }

    infix fun whenever(filter: (E) -> Boolean) = apply {
        this.filter = filter
    }

    var transform: ((LoadingState.Failure<T>) -> LoadingState<T>) = { it }

    infix fun map(transform: (LoadingState.Failure<T>) -> LoadingState<T>) = apply {
        this.transform = transform
    }

    fun handle(): LoadingState<T> {
        when (state) {
            is LoadingState.Failure -> {
                if (klass.isInstance(state.error) && filter(state.error as E)) {
                    return transform(state)
                } else {
                    return state
                }
            }
            else -> return state
        }
    }
}