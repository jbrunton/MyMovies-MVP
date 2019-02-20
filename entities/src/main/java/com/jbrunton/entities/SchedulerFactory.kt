package com.jbrunton.entities

import io.reactivex.Scheduler

interface SchedulerFactory {
    val Main: Scheduler
    val IO: Scheduler
}