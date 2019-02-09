package com.jbrunton.mymovies.helpers

import com.jbrunton.async.AsyncResult
import com.jbrunton.async.zipWith

class AsyncResults {
    companion object {
        fun <T1, T2, R> zip(
                result1: AsyncResult<T1>,
                result2: AsyncResult<T2>,
                zipper: (T1, T2) -> R
        ) = result1.zipWith(result2, zipper)

        fun <T1, T2, T3, R> zip(
                result1: AsyncResult<T1>,
                result2: AsyncResult<T2>,
                result3: AsyncResult<T3>,
                zipper: (T1, T2, T3) -> R
        ): AsyncResult<R> {
            val partialZipper = { r1: T1, r2 : T2 -> { r3: T3 -> zipper(r1, r2, r3) } }
            return result1.zipWith(result2, partialZipper).zipWith(result3) {
                p, r3 -> p(r3)
            }
        }
    }
}
