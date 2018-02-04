package com.jbrunton.mymovies.helpers

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer

fun <T> LiveData<T>.observe(owner: LifecycleOwner, observer: (T) -> Unit) =
        this.observe(owner, Observer { observer(it!!) })
