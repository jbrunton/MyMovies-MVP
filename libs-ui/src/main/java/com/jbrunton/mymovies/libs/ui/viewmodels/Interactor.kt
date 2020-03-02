package com.jbrunton.mymovies.libs.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.distinctUntilChanged
import com.snakydesign.livedataextensions.scan
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect

abstract class Interactor<Intent, State, Action> {
    protected abstract val initialState: State

    private val changes = MutableLiveData<Action>()

    val state = lazy {
        changes.scan(initialState, ::combine).distinctUntilChanged()
    }

    abstract suspend fun actionsFor(intent: Intent): Flow<Action>

    // TODO: consider whether to reduce viewstate instead. See e.g.
    // https://medium.com/mindorks/mvi-a-reactive-architecture-pattern-45c6f5096ab7
    // (also consider whether to replace flows with livedata)

    // might be nice to be able to define intents as LiveData
    // e.g.
    //  intent(DiscoverIntent.Load).switchMap {
    //      useCase.discover()
    //          .map { DiscoverStateAction.DiscoverResultsAvailable(it) }
    //          .collect { applyAction(it) }
    // }
    //

    // Also consider separating actions and reducer like this:
    // https://proandroiddev.com/mvi-a-new-member-of-the-mv-band-6f7f0d23bc8a
    abstract fun combine(previousState: State, change: Action): State

    suspend fun perform(intent: Intent) {
        actionsFor(intent).collect { change ->
            changes.postValue(change)
        }
    }
}
