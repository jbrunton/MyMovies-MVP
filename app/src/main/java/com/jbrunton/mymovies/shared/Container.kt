package com.jbrunton.mymovies.shared

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.jbrunton.mymovies.nav.Navigator
import org.koin.core.parameter.ParameterList
import org.koin.core.parameter.emptyParameterDefinition
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
        Log.d("Container", "Trying to resolve type ${klass.qualifiedName}")
        return tryResolveSingleton(klass, parameters)
                ?: tryResolveFactory(klass, parameters)
                ?: parent?.let { resolve(klass, parameters) }
                ?: throw NullPointerException("Unable to resolve type ${klass.qualifiedName}")
    }

    fun <T : ViewModel> resolveViewModel(
            activity: FragmentActivity,
            klass: KClass<T>,
            parameters: ParameterDefinition = emptyParameterDefinition()
    ): T {
        return ViewModelProviders.of(activity, object : ViewModelProvider.Factory {
            override fun <S : ViewModel> create(modelClass: Class<S>): S {
                return resolve(klass, parameters) as S
            }
        }).get(klass.java)
    }

    fun <T : ViewModel> resolveViewModel(
            fragment: Fragment,
            klass: KClass<T>,
            parameters: ParameterDefinition = emptyParameterDefinition()
    ): T {
        return ViewModelProviders.of(fragment, object : ViewModelProvider.Factory {
            override fun <S : ViewModel> create(modelClass: Class<S>): S {
                return resolve(klass, parameters) as S
            }
        }).get(klass.java)
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
