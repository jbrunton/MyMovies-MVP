package com.jbrunton.mymovies.features.discover

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import com.snakydesign.livedataextensions.distinctUntilChanged
import com.snakydesign.livedataextensions.filter
import com.snakydesign.livedataextensions.map
import com.snakydesign.livedataextensions.scan

abstract class Interactor<Intent, State, Change> {

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

    protected abstract fun dispatch(intent: Intent): LiveData<Change>
    protected abstract fun combine(previousState: State, change: Change): State
}
