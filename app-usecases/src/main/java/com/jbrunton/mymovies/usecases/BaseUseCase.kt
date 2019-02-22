package com.jbrunton.mymovies.usecases

import com.jbrunton.entities.HasSchedulers
import com.jbrunton.entities.SchedulerContext

open class BaseUseCase : HasSchedulers {
    override lateinit var schedulerContext: SchedulerContext

    fun start(schedulerContext: SchedulerContext) {
        this.schedulerContext = schedulerContext
    }
}