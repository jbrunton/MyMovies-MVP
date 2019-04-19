package com.jbrunton.mymovies.ui.shared

import com.jbrunton.entities.HasSchedulers
import com.jbrunton.entities.SchedulerContext
import com.jbrunton.libs.ui.NavigationRequest
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