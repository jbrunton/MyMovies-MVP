package com.jbrunton.mymovies.shared

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import org.koin.core.parameter.ParameterList
import org.koin.core.parameter.emptyParameterDefinition
import java.lang.NullPointerException
import kotlin.reflect.KClass

class Container {
    val singletonRegistry = HashMap<KClass<*>, Any>()
    val singletonDefinitions = HashMap<KClass<*>, Definition<*>>()
    val factoryDefinitions = HashMap<KClass<*>, Definition<*>>()

    inline fun <reified T : Any> single(noinline definition: Definition<T>) {
        singletonDefinitions.put(T::class, definition)
    }

    inline fun <reified T : Any> factory(noinline definition: Definition<T>) {
        factoryDefinitions.put(T::class, definition)
    }

    inline fun <reified T : Any> get(): T {
        return resolve(T::class)
    }

    fun <T : Any> resolve(klass: KClass<T>, parameters: ParameterDefinition = emptyParameterDefinition()): T {
        return tryResolveSingleton(klass, parameters)
                ?: tryResolveFactory(klass, parameters)
                ?: throw NullPointerException("Unable to resolve type: ${klass.qualifiedName}")
    }

    private fun <T : Any> tryResolveSingleton(klass: KClass<T>, parameters: ParameterDefinition): T? {
        var instance = singletonRegistry.get(klass) as T?
        if (instance == null) {
            instance = singletonDefinitions.get(klass)?.invoke(parameters()) as T?
            if (instance != null) {
                singletonRegistry.put(klass, instance)
            }
        }
        return instance
    }

    private fun <T : Any> tryResolveFactory(klass: KClass<T>, parameters: ParameterDefinition): T? {
        return factoryDefinitions.get(klass)?.invoke(parameters()) as T?
    }
}

typealias Definition<T> = (ParameterList) -> T

typealias ParameterDefinition = () -> ParameterList
