package com.jbrunton.mymovies.usecases.shared

import com.jbrunton.async.AsyncResult
import com.jbrunton.async.flatMap
import com.jbrunton.mymovies.ui.shared.LoadingViewState
import com.jbrunton.mymovies.ui.shared.toLoadingViewState

fun<S, V> viewStatePipeline(
        result: AsyncResult<S>,
        emptyState: V,
        transform: (S) -> AsyncResult<V>
): LoadingViewState<V> =
        result.flatMap(transform).toLoadingViewState(emptyState)
