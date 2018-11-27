package com.jbrunton.mymovies.di

import com.jbrunton.mymovies.di.Container

interface HasContainer {
    val container: Container
}

inline fun <reified T: Any> HasContainer.resolve(noinline parameters: ParameterDefinition = emptyParameterDefinition()): T {
    return container.resolve(T::class, parameters)
}
