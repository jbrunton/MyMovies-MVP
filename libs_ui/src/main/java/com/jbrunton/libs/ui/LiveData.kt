package com.jbrunton.libs.ui

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

fun <T> LiveData<T>.observe(owner: LifecycleOwner, observer: (T) -> Unit) =
        this.observe(owner, Observer { observer(it!!) })
