package com.jbrunton.mymovies.di

import java.lang.NullPointerException
import kotlin.reflect.KClass

class Container(val parent: Container? = null) {
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

    fun createChildContainer() = Container(this)

    fun <T : Any> resolve(klass: KClass<T>, parameters: ParameterDefinition = emptyParameterDefinition()): T {
        return tryResolveSingleton(klass, parameters)
                ?: tryResolveFactory(klass, parameters)
                ?: parent?.resolve(klass, parameters)
                ?: throw ResolutionFailure(klass)
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
