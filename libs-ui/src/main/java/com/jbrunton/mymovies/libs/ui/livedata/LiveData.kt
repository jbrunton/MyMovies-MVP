package com.jbrunton.mymovies.libs.ui.livedata

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

fun <T> LiveData<T>.observe(owner: LifecycleOwner, observer: (T) -> Unit) =
        this.observe(owner, Observer { observer(it!!) })
