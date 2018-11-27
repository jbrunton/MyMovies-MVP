package com.jbrunton.mymovies.di

import java.lang.NullPointerException
import kotlin.reflect.KClass

class Container(val parent: Container? = null) {
    private val singletonRegistry = HashMap<KClass<*>, Any>()
    private val singletonDefinitions = HashMap<KClass<*>, Definition<*>>()
    private val factoryDefinitions = HashMap<KClass<*>, Definition<*>>()

    inline fun <reified T : Any> single(override: Boolean = false, noinline definition: Definition<T>) {
        registerSingleton(T::class, override, definition)
    }

    inline fun <reified T : Any> factory(override: Boolean = false, noinline definition: Definition<T>) {
        registerFactory(T::class, override, definition)
    }

    inline fun <reified T : Any> get(): T {
        return resolve(T::class)
    }

    fun createChildContainer() = Container(this)

    fun isRegistered(klass: KClass<*>): Boolean = singletonDefinitions.containsKey(klass)
            || factoryDefinitions.containsKey(klass)
            || parent?.isRegistered(klass) ?: false

    fun <T : Any> resolve(klass: KClass<T>, parameters: ParameterDefinition = emptyParameterDefinition()): T {
        return tryResolveSingleton(klass, parameters)
                ?: tryResolveFactory(klass, parameters)
                ?: parent?.resolve(klass, parameters)
                ?: throw ResolutionFailure(klass)
    }

    fun <T : Any> registerSingleton(klass: KClass<T>, override: Boolean = false, definition: Definition<T>) {
        checkNewType(klass, override)
        singletonDefinitions.put(klass, definition)
    }

    fun <T : Any> registerFactory(klass: KClass<T>, override: Boolean = false, definition: Definition<T>) {
        checkNewType(klass, override)
        factoryDefinitions.put(klass, definition)
    }

    fun registerModules(vararg modules: Module) {
        for (module in modules) {
            module.registerTypes(this)
        }
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

    private fun checkNewType(klass: KClass<*>, override: Boolean) {
        if (override) return
        if (isRegistered(klass)) {
            throw TypeAlreadyRegistered(klass)
        }
    }
}

typealias Definition<T> = (ParameterList) -> T

interface Module {
    fun registerTypes(container: Container)
}