package com.jbrunton.libs.ui

import androidx.annotation.DrawableRes
import androidx.lifecycle.ViewModel
import com.jbrunton.async.AsyncResult
import com.jbrunton.entities.HasSchedulers
import com.jbrunton.entities.SchedulerContext
import com.jbrunton.entities.SchedulerFactory
import com.jbrunton.entities.subscribe
import com.jbrunton.inject.Container
import com.jbrunton.inject.HasContainer
import com.jbrunton.inject.inject

abstract class BaseViewModel(override val container: Container) : ViewModel(),
        HasSchedulers, HasContainer, NavigationResultListener
{
    val schedulerFactory: SchedulerFactory by inject()
    val navigator: Navigator by inject()
    override val schedulerContext = SchedulerContext(schedulerFactory)
    val snackbar = SingleLiveEvent<SnackbarEvent>()

    open fun start() {
        navigator.register(this)
    }

    fun start(useCase: BaseUseCase) {
        subscribe(useCase.navigationRequest, navigator::navigate)
        useCase.start(schedulerContext)
    }

    fun<T>failure(errorMessage: String,
                  @DrawableRes errorIcon: Int = 0,
                  allowRetry: Boolean = false
    ): AsyncResult<T> {
        return AsyncResult.Failure(LoadingViewStateError(errorMessage, errorIcon, allowRetry))
    }

    override fun onNavigationResult(result: NavigationResult) {}

    override fun onCleared() {
        navigator.unregister(this)
        schedulerContext.dispose()
        super.onCleared()
    }
}
