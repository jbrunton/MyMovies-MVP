package com.jbrunton.entities

import io.reactivex.ObservableTransformer
import io.reactivex.Scheduler

interface SchedulerFactory {
    val Main: Scheduler
    val IO: Scheduler

    fun <T> apply() = ObservableTransformer<T, T> {
        it.subscribeOn(IO).observeOn(Main)
    }
}