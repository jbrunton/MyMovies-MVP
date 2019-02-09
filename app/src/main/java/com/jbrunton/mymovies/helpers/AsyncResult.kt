package com.jbrunton.mymovies.helpers

import com.jbrunton.async.AsyncResult
import com.jbrunton.async.zipWith

class AsyncResults {
    companion object {
        fun <T1, T2, R> zip(
                result1: AsyncResult<T1>,
                result2: AsyncResult<T2>,
                zipper: (T1, T2) -> R
        ): AsyncResult<R> {
            return result1.zipWith(result2, zipper)
        }

        fun <T1, T2, T3, R> zip(
                result1: AsyncResult<T1>,
                result2: AsyncResult<T2>,
                result3: AsyncResult<T3>,
                zipper: (T1, T2, T3) -> R
        ): AsyncResult<R> {
            val partialZipper = { r1: T1, r2 : T2 -> { r3: T3 -> zipper(r1, r2, r3) } }
            return zip(result1, result2, partialZipper)
                    .zipWith(result3) { p, r3 -> p(r3) }
        }

        fun <T1, T2, T3, T4, R> zip(
                result1: AsyncResult<T1>,
                result2: AsyncResult<T2>,
                result3: AsyncResult<T3>,
                result4: AsyncResult<T4>,
                zipper: (T1, T2, T3, T4) -> R
        ): AsyncResult<R> {
            val partialZipper = { r1: T1, r2: T2, r3: T3 -> { r4: T4 -> zipper(r1, r2, r3, r4) } }
            return zip(result1, result2, result3, partialZipper)
                    .zipWith(result4) { p, r4 -> p(r4) }
        }

        fun <T1, T2, T3, T4, T5, R> zip(
                result1: AsyncResult<T1>,
                result2: AsyncResult<T2>,
                result3: AsyncResult<T3>,
                result4: AsyncResult<T4>,
                result5: AsyncResult<T5>,
                zipper: (T1, T2, T3, T4, T5) -> R
        ): AsyncResult<R> {
            val partialZipper = { r1: T1, r2: T2, r3: T3, r4: T4 -> { r5: T5 -> zipper(r1, r2, r3, r4, r5) } }
            return zip(result1, result2, result3, result4, partialZipper)
                    .zipWith(result5) { p, r5 -> p(r5) }
        }
    }
}
