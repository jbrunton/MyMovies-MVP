package com.jbrunton.entities.models

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import javax.xml.ws.http.HTTPException

class LoadingStateTest {
    val value = 123
    val cachedValue = 456
    val error = Throwable("error")

    @Test
    fun convertsSuccessToValueOrNull() {
        assertThat(successState(value).toValueOrNull()).isEqualTo(value)
    }

    @Test
    fun convertsLoadingToValueOrNull() {
        assertThat(loadingState(cachedValue).toValueOrNull()).isEqualTo(cachedValue)
        assertThat(loadingState(null).toValueOrNull()).isNull()
    }

    @Test
    fun convertsFailureToValueOrNull() {
        assertThat(failureState(error, cachedValue).toValueOrNull()).isEqualTo(cachedValue)
        assertThat(failureState(error,null).toValueOrNull()).isNull()
    }

    @Test
    fun transformsSuccessfulStates() {
        val state = successState(2).map { it * 2 }
        assertThat(state.toValue()).isEqualTo(4)
    }

    @Test
    fun transformsLoadingStates() {
        val state = loadingState(2).map { it * 2 }
        assertThat(state.toValue()).isEqualTo(4)
    }

    @Test
    fun transformsFailureStates() {
        val state = failureState(error, 2).map { it * 2 }
        assertThat(state.toValue()).isEqualTo(4)
    }

    @Test
    fun transformsOnlySuccessStates() {
        val state = successState(1).onSuccess {
            LoadingState.Success(it.value * 2)
        }
        assertThat(state).isEqualTo(LoadingState.Success(2))
    }

    @Test
    fun doesNotTransformOtherStatesWhenExpectingSuccess() {
        loadingState(null).onSuccess { throw IllegalStateException() }
        failureState(error,null).onSuccess { throw IllegalStateException() }
    }

    @Test
    fun transformsOnlyLoadingStates() {
        val state = loadingState(1).onLoading {
            LoadingState.Success(it.toValue() * 2)
        }
        assertThat(state).isEqualTo(LoadingState.Success(2))
    }

    @Test
    fun doesNotTransformOtherStatesWhenExpectingLoading() {
        successState(value).onLoading { throw IllegalStateException() }
        failureState(error,null).onLoading { throw IllegalStateException() }
    }

    @Test
    fun transformsOnlyFailureStates() {
        val state = failureState(error,1).onFailure {
            LoadingState.Success(it.toValue() * 2)
        }
        assertThat(state).isEqualTo(LoadingState.Success(2))
    }

    @Test
    fun doesNotTransformOtherStatesWhenExpectingFailure() {
        successState(value).onFailure { throw IllegalStateException() }
        loadingState(value).onFailure { throw IllegalStateException() }
    }

    @Test
    fun transformsErrors() {
        val state = failureState(HTTPException(401), 1).onError(HTTPException::class) {
            map { LoadingState.Success(it.toValue() * 2) }
        }
        assertThat(state).isEqualTo(LoadingState.Success(2))
    }

    @Test
    fun transformsSpecificErrors() {
        val failureState = failureState(HTTPException(401), 1)

        val transformedState = failureState.onError(HTTPException::class) {
            map { LoadingState.Success(it.toValue() * 2) } whenever { it.statusCode == 401 }
        }
        val otherState = failureState.onError(HTTPException::class) {
            map { LoadingState.Success(it.toValue() * 2) } whenever { it.statusCode == 400 }
        }

        assertThat(transformedState).isEqualTo(LoadingState.Success(2))
        assertThat(otherState).isEqualTo(failureState)
    }

    private fun successState(value: Int): LoadingState<Int> {
        return LoadingState.Success(value)
    }

    private fun loadingState(cachedValue: Int?): LoadingState<Int> {
        return LoadingState.Loading(cachedValue)
    }

    private fun failureState(error: Throwable, cachedValue: Int?): LoadingState<Int> {
        return LoadingState.Failure(error, cachedValue)
    }
}