package com.jbrunton.mymovies.entities

import io.reactivex.Scheduler

interface SchedulerFactory {
    val Main: Scheduler
    val IO: Scheduler
}