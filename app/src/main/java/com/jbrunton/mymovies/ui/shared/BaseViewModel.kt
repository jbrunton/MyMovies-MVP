package com.jbrunton.mymovies.ui.shared

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
import com.jbrunton.mymovies.nav.Navigator
import com.jbrunton.mymovies.usecases.BaseUseCase
import com.jbrunton.mymovies.usecases.nav.NavigationResult

abstract class BaseViewModel(override val container: Container) : ViewModel(), HasSchedulers, HasContainer {
    val schedulerFactory: SchedulerFactory by inject()
    val navigator: Navigator by inject()
    override val schedulerContext = SchedulerContext(schedulerFactory)
    val snackbar = SingleLiveEvent<SnackbarEvent>()

    abstract fun start()

    fun start(useCase: BaseUseCase) {
        subscribe(useCase.navigationRequest, navigator::navigate)
        navigator.register(useCase)
        useCase.start(schedulerContext)
    }

    fun<T>failure(errorMessage: String,
                  @DrawableRes errorIcon: Int = 0,
                  allowRetry: Boolean = false
    ): AsyncResult<T> {
        return AsyncResult.Failure(LoadingViewStateError(errorMessage, errorIcon, allowRetry))
    }

    open fun onNavigationResult(result: NavigationResult) {}

    override fun onCleared() {
        schedulerContext.dispose()
        super.onCleared()
    }
}
