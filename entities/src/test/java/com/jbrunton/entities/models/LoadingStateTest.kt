package com.jbrunton.entities.models

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class LoadingStateTest {
    val value = 123
    val cachedValue = 456
    val error = Throwable("error")

    @Test
    fun convertsSuccessToValueOrNull() {
        assertThat(success(value).toValueOrNull()).isEqualTo(value)
    }

    @Test
    fun convertsLoadingToValueOrNull() {
        assertThat(loading(cachedValue).toValueOrNull()).isEqualTo(cachedValue)
        assertThat(loading(null).toValueOrNull()).isNull()
    }

    @Test
    fun convertsFailureToValueOrNull() {
        assertThat(failure(error, cachedValue).toValueOrNull()).isEqualTo(cachedValue)
        assertThat(failure(error,null).toValueOrNull()).isNull()
    }

    @Test
    fun transformsSuccessfulStates() {
        val state = success(2).map { it * 2 }
        assertThat(state.toValue()).isEqualTo(4)
    }

    @Test
    fun transformsLoadingStates() {
        val state = loading(2).map { it * 2 }
        assertThat(state.toValue()).isEqualTo(4)
    }

    @Test
    fun transformsFailureStates() {
        val state = failure(error, 2).map { it * 2 }
        assertThat(state.toValue()).isEqualTo(4)
    }

    @Test
    fun transformsOnlySuccessStates() {
        val state = success(1).onSuccess {
            LoadingState.Success(it.value * 2)
        }
        assertThat(state).isEqualTo(LoadingState.Success(2))
    }

    @Test
    fun doesNotInterceptOtherStatesWhenExpectingSuccess() {
        loading(null).onSuccess { throw IllegalStateException() }
        failure(error,null).onSuccess { throw IllegalStateException() }
    }

    @Test
    fun transformsOnlyLoadingStates() {
        val state = loading(1).onLoading {
            LoadingState.Success(it.toValue() * 2)
        }
        assertThat(state).isEqualTo(LoadingState.Success(2))
    }

    @Test
    fun doesNotInterceptOtherStatesWhenExpectingLoading() {
        success(value).onLoading { throw IllegalStateException() }
        failure(error,null).onLoading { throw IllegalStateException() }
    }

    private fun success(value: Int): LoadingState<Int> {
        return LoadingState.Success(value)
    }

    private fun loading(cachedValue: Int?): LoadingState<Int> {
        return LoadingState.Loading(cachedValue)
    }

    private fun failure(error: Throwable, cachedValue: Int?): LoadingState<Int> {
        return LoadingState.Failure(error, cachedValue)
    }
}