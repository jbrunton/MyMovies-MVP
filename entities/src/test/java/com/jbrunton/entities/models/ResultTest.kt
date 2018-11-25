package com.jbrunton.entities.models

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import javax.xml.ws.http.HTTPException

class ResultTest {
    val value = 123
    val cachedValue = 456
    val error = Throwable("error")

    @Test
    fun convertsSuccessToValueOrNull() {
        assertThat(successState(value).getOrNull()).isEqualTo(value)
    }

    @Test
    fun convertsLoadingToValueOrNull() {
        assertThat(loadingState(cachedValue).getOrNull()).isEqualTo(cachedValue)
        assertThat(loadingState(null).getOrNull()).isNull()
    }

    @Test
    fun convertsFailureToValueOrNull() {
        assertThat(failureState(error, cachedValue).getOrNull()).isEqualTo(cachedValue)
        assertThat(failureState(error,null).getOrNull()).isNull()
    }

    @Test
    fun transformsSuccessfulStates() {
        val state = successState(2).map { it * 2 }
        assertThat(state.get()).isEqualTo(4)
    }

    @Test
    fun transformsLoadingStates() {
        val state = loadingState(2).map { it * 2 }
        assertThat(state.get()).isEqualTo(4)
    }

    @Test
    fun transformsFailureStates() {
        val state = failureState(error, 2).map { it * 2 }
        assertThat(state.get()).isEqualTo(4)
    }

    @Test
    fun transformsOnlySuccessStates() {
        val state = successState(1).onSuccess {
            Result.Success(it.value * 2)
        }
        assertThat(state).isEqualTo(Result.Success(2))
    }

    @Test
    fun doesNotTransformOtherStatesWhenExpectingSuccess() {
        loadingState(null).onSuccess { throw IllegalStateException() }
        failureState(error,null).onSuccess { throw IllegalStateException() }
    }

    @Test
    fun transformsOnlyLoadingStates() {
        val state = loadingState(1).onLoading {
            Result.Success(it.get() * 2)
        }
        assertThat(state).isEqualTo(Result.Success(2))
    }

    @Test
    fun doesNotTransformOtherStatesWhenExpectingLoading() {
        successState(value).onLoading { throw IllegalStateException() }
        failureState(error,null).onLoading { throw IllegalStateException() }
    }

    @Test
    fun transformsOnlyFailureStates() {
        val state = failureState(error,1).onFailure {
            Result.Success(it.get() * 2)
        }
        assertThat(state).isEqualTo(Result.Success(2))
    }

    @Test
    fun doesNotTransformOtherStatesWhenExpectingFailure() {
        successState(value).onFailure { throw IllegalStateException() }
        loadingState(value).onFailure { throw IllegalStateException() }
    }

    @Test
    fun transformsErrors() {
        val state = failureState(HTTPException(401), 1).onError(HTTPException::class) {
            map { Result.Success(it.get() * 2) }
        }
        assertThat(state).isEqualTo(Result.Success(2))
    }

    @Test
    fun transformsSpecificErrors() {
        val failureState = failureState(HTTPException(401), 1)

        val transformedState = failureState.onError(HTTPException::class) {
            map { Result.Success(it.get() * 2) } whenever { it.statusCode == 401 }
        }
        val otherState = failureState.onError(HTTPException::class) {
            map { Result.Success(it.get() * 2) } whenever { it.statusCode == 400 }
        }

        assertThat(transformedState).isEqualTo(Result.Success(2))
        assertThat(otherState).isEqualTo(failureState)
    }

    @Test
    fun zipsSuccesses() {
        val state = successState(2).zipWith(successState(3)) { x, y -> x * y }
        assertThat(state).isEqualTo(Result.Success(6))
    }

    @Test
    fun zipsLeftFailures() {
        val state = failureState(error, 2).zipWith(successState(3)) { x, y -> x * y }
        assertThat(state).isEqualTo(Result.Failure(error, 6))
    }

    @Test
    fun zipsRightFailures() {
        val state = successState(3).zipWith(failureState(error, 2)) { x, y -> x * y }
        assertThat(state).isEqualTo(Result.Failure(error, 6))
    }

    @Test
    fun zipsLeftLoadingStates() {
        val state = loadingState(2).zipWith(successState(3)) { x, y -> x * y }
        assertThat(state).isEqualTo(Result.Loading(6))
    }

    @Test
    fun zipsRightLoadingStates() {
        val state = successState(2).zipWith(loadingState(3)) { x, y -> x * y }
        assertThat(state).isEqualTo(Result.Loading(6))
    }

    private fun successState(value: Int): Result<Int> {
        return Result.Success(value)
    }

    private fun loadingState(cachedValue: Int?): Result<Int> {
        return Result.Loading(cachedValue)
    }

    private fun failureState(error: Throwable, cachedValue: Int?): Result<Int> {
        return Result.Failure(error, cachedValue)
    }
}