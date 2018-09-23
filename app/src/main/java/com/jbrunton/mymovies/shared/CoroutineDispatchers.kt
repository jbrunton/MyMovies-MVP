package com.jbrunton.mymovies.shared

import kotlinx.coroutines.CoroutineDispatcher

data class CoroutineDispatchers(
        val Main: CoroutineDispatcher,
        val IO: CoroutineDispatcher
)
