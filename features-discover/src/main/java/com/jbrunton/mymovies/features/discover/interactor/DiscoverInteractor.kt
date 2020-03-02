package com.jbrunton.mymovies.features.discover.interactor

import com.jbrunton.async.AsyncResult
import com.jbrunton.mymovies.libs.ui.viewmodels.Dispatcher
import com.jbrunton.mymovies.libs.ui.viewmodels.Interactor
import com.jbrunton.mymovies.libs.ui.viewmodels.Reducer
import com.jbrunton.mymovies.usecases.discover.DiscoverState
import com.jbrunton.mymovies.usecases.discover.DiscoverUseCase

class DiscoverInteractor(
        useCase: DiscoverUseCase,
        callbacks: Callbacks
) : Interactor<DiscoverIntent, AsyncResult<DiscoverState>, DiscoverStateChange>(),
        Reducer<AsyncResult<DiscoverState>, DiscoverStateChange> by DiscoverStateReducer(callbacks),
        Dispatcher<DiscoverIntent, DiscoverStateChange> by DiscoverIntentDispatcher(useCase, callbacks)
{
    interface Callbacks : DiscoverIntentDispatcher.Callbacks, DiscoverStateReducer.Callbacks

    override val initialState: AsyncResult<DiscoverState> = AsyncResult.loading(null)
}
