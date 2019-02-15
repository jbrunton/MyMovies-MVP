package com.jbrunton.mymovies.usecases

import io.reactivex.ObservableTransformer
import io.reactivex.Scheduler

open class SchedulerFactory(
    val Main: Scheduler,
    val IO: Scheduler
) {
    fun <T> apply(): ObservableTransformer<T, T> {
        return ObservableTransformer {
            it.subscribeOn(IO)
                    .observeOn(Main)
        }
    }
}