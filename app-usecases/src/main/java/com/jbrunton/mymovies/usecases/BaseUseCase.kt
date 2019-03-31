package com.jbrunton.mymovies.usecases

import com.jbrunton.entities.HasSchedulers
import com.jbrunton.entities.SchedulerContext
import com.jbrunton.mymovies.usecases.nav.NavigationRequest
import com.jbrunton.mymovies.usecases.nav.NavigationResult
import com.jbrunton.mymovies.usecases.nav.NavigationResultListener
import io.reactivex.subjects.PublishSubject

open class BaseUseCase : HasSchedulers, NavigationResultListener {
    override lateinit var schedulerContext: SchedulerContext

    val navigationRequest = PublishSubject.create<NavigationRequest>()

    fun navigate(request: NavigationRequest) {
        navigationRequest.onNext(request)
    }

    open fun start(schedulerContext: SchedulerContext) {
        this.schedulerContext = schedulerContext
    }

    override open fun onNavigationResult(result: NavigationResult) {}
}