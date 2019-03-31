package com.jbrunton.mymovies.ui.shared

import com.jbrunton.async.AsyncResult
import com.jbrunton.async.flatMap

class LoadingViewStateBuilder<V>(val emptyState: V) {
    fun <S> flatMap(result: AsyncResult<S>, transform: (S) -> AsyncResult<V>): LoadingViewState<V> =
            result.flatMap(transform).toLoadingViewState(emptyState)

    fun <S> map(result: AsyncResult<S>, transform: (S) -> V): LoadingViewState<V> =
            result.flatMap { AsyncResult.success(transform(it)) }.toLoadingViewState(emptyState)
}
