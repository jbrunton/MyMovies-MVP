package com.jbrunton.mymovies.shared

import java.lang.NullPointerException
import kotlin.reflect.KClass

class Container {
    val instanceRegistry = HashMap<KClass<*>, Any>()
    val factoryRegistry = HashMap<KClass<*>, InstanceFactory>()

    inline fun <reified T : Any> single(instance: T) {
        instanceRegistry.put(T::class, instance)
    }

    inline fun <reified T : Any> factory(noinline factory: () -> T) {
        factoryRegistry.put(T::class, factory)
    }

    inline fun <reified T : Any>get(): T {
        return instanceRegistry.get(T::class) as T?
                ?: factoryRegistry.get(T::class)?.invoke() as T
    }
}

typealias InstanceFactory = () -> Any