package com.jbrunton.mymovies.shared

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import kotlin.reflect.KClass

interface HasContainer {
    val container: Container
}

inline fun <reified T: Any> HasContainer.resolve(noinline parameters: ParameterDefinition = emptyParameterDefinition()): T {
    return container.resolve(T::class, parameters)
}
