package com.jbrunton.mymovies.shared

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

data class CoroutineDispatchers(
        val Main: CoroutineDispatcher = Dispatchers.Main,
        val IO: CoroutineDispatcher = Dispatchers.IO
)
