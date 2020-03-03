package com.jbrunton.mymovies.features.discover.interactor

import com.jbrunton.async.AsyncResult
import com.jbrunton.mymovies.libs.ui.viewmodels.Interactor
import com.jbrunton.mymovies.usecases.discover.DiscoverState
import com.jbrunton.mymovies.usecases.discover.DiscoverUseCase

class DiscoverInteractor(
        initialState: AsyncResult<DiscoverState>,
        useCase: DiscoverUseCase,
        callbacks: Callbacks
) : Interactor<DiscoverIntent, AsyncResult<DiscoverState>, DiscoverStateChange>(
        initialState,
        DiscoverIntentDispatcher(useCase, callbacks),
        DiscoverStateReducer(callbacks)
) {
    interface Callbacks : DiscoverIntentDispatcher.Callbacks, DiscoverStateReducer.Callbacks
}
