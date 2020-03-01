package com.jbrunton.mymovies.fixtures

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import org.assertj.core.api.Assertions.assertThat

class TestFlowCollector<T>(private val flow: Flow<T>) {
    suspend fun assertValues(vararg values: T) {
        assertThat(flow.toList()).isEqualTo(values.toList())
    }

    @ExperimentalCoroutinesApi
    suspend fun assertValuesThenCancel(vararg values: T) {
        assertThat(flow.take(values.size).toList()).isEqualTo(values.toList())
    }
}

fun <T> Flow<T>.test() = TestFlowCollector<T>(this)
