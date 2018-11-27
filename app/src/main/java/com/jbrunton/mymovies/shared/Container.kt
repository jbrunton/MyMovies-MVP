package com.jbrunton.mymovies.shared

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
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

    fun <T : Any> resolve(klass: KClass<T>): T {
        return tryResolveSingleton(klass)
                ?: tryResolveFactory(klass)
                ?: throw NullPointerException("Unable to resolve type: ${klass.qualifiedName}")
    }

    private fun <T : Any> tryResolveSingleton(klass: KClass<T>): T? {
        var instance = singletonRegistry.get(klass) as T?
        if (instance == null) {
            instance = singletonDefinitions.get(klass)?.invoke() as T?
            if (instance != null) {
                singletonRegistry.put(klass, instance)
            }
        }
        return instance
    }

    private fun <T : Any> tryResolveFactory(klass: KClass<T>): T? {
        return factoryDefinitions.get(klass)?.invoke() as T?
    }
}

typealias Definition<T> = () -> T
