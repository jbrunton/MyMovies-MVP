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