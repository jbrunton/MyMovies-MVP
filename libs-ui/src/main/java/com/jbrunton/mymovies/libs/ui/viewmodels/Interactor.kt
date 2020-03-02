package com.jbrunton.mymovies.libs.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import com.snakydesign.livedataextensions.distinctUntilChanged
import com.snakydesign.livedataextensions.scan

interface Dispatcher<Intent, Change> {
    fun dispatch(intent: Intent): LiveData<Change>
}

interface Reducer<State, Change> {
    fun combine(previousState: State, change: Change): State
}

abstract class Interactor<Intent, State, Change>
    : Dispatcher<Intent, Change>, Reducer<State, Change>
{

    protected abstract val initialState: State
    private val intents = MutableLiveData<Intent>()
    private val changes: LiveData<Change> by lazy { intents.switchMap { dispatch(it) } }

    val state: LiveData<State> by lazy {
        changes.scan(initialState, ::combine)
                .distinctUntilChanged()
    }

    fun perform(intent: Intent) {
        intents.postValue(intent)
    }
}
