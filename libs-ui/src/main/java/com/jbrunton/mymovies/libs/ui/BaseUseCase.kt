package com.jbrunton.mymovies.libs.ui

import com.jbrunton.mymovies.entities.HasSchedulers
import com.jbrunton.mymovies.entities.SchedulerContext
import io.reactivex.subjects.PublishSubject

open class BaseUseCase : HasSchedulers {
    override lateinit var schedulerContext: SchedulerContext

    val navigationRequest = PublishSubject.create<NavigationRequest>()

    fun navigate(request: NavigationRequest) {
        navigationRequest.onNext(request)
    }

    open fun start(schedulerContext: SchedulerContext) {
        this.schedulerContext = schedulerContext
    }
}