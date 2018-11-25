package com.jbrunton.entities.models

import io.reactivex.Observable
import kotlin.reflect.KClass

sealed class Result<out T> {
    data class Success<T>(val value: T): Result<T>()
    data class Failure<T>(val error: Throwable, val cachedValue: T? = null): Result<T>()
    data class Loading<T>(val cachedValue: T? = null): Result<T>()
}

typealias DataStream<T> = Observable<Result<T>>

fun <T> Result<T>.get(): T {
    return when (this) {
        is Result.Success -> this.value
        is Result.Loading -> this.cachedValue ?: throw NullPointerException()
        is Result.Failure -> this.cachedValue ?: throw this.error
    }
}

fun <T> Result<T>.getOr(defaultValue: T): T {
    return when (this) {
        is Result.Success -> this.value
        is Result.Loading -> this.cachedValue ?: defaultValue
        is Result.Failure -> this.cachedValue ?: defaultValue
    }
}

fun <T> Result<T>.getOrNull(): T? {
    return getOr(null)
}

fun <S, T, U> Result<S>.zipWith(other: Result<T>, transform: (S, T) -> U): Result<U> {
    if (this is Result.Success && other is Result.Success) {
        return Result.Success(transform(this.value, other.value))
    }

    val cachedValue = this.getOrNull()?.let { thisValue ->
        other.getOrNull()?.let { otherValue -> transform(thisValue, otherValue) }
    }

    return if (this is Result.Failure) {
        Result.Failure(this.error, cachedValue)
    } else if (other is Result.Failure) {
        Result.Failure(other.error, cachedValue)
    } else {
        Result.Loading(cachedValue)
    }
}

fun <S, T> Result<S>.map(transform: (S) -> T): Result<T> {
    return when (this) {
        is Result.Success -> Result.Success(transform(this.value))
        is Result.Loading -> Result.Loading(this.cachedValue?.let(transform))
        is Result.Failure -> Result.Failure(this.error, this.cachedValue?.let(transform))
    }
}

fun <T> Result<T>.onSuccess(transform: (Result.Success<T>) -> Result<T>): Result<T> {
    return when (this) {
        is Result.Success -> transform(this)
        else -> this
    }
}

fun <T> Result<T>.onLoading(transform: (Result.Loading<T>) -> Result<T>): Result<T> {
    return when (this) {
        is Result.Loading -> transform(this)
        else -> this
    }
}

fun <T> Result<T>.onFailure(transform: (Result.Failure<T>) -> Result<T>): Result<T> {
    return when (this) {
        is Result.Failure -> transform(this)
        else -> this
    }
}

fun <T, E: Throwable> Result<T>.onError(
        klass: KClass<E>,
        block: ErrorHandler<T, E>.() -> Unit
) = ErrorHandler(klass, this).apply(block).handle()

class ErrorHandler<T, E: Throwable>(val klass: KClass<E>, val state: Result<T>) {
    var filter: (E) -> Boolean = { true }

    infix fun whenever(filter: (E) -> Boolean) = apply {
        this.filter = filter
    }

    var transform: ((Result.Failure<T>) -> Result<T>) = { it }

    infix fun map(transform: (Result.Failure<T>) -> Result<T>) = apply {
        this.transform = transform
    }

    fun handle(): Result<T> {
        when (state) {
            is Result.Failure -> {
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