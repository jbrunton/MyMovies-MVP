package com.jbrunton.mymovies.shared

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.jbrunton.mymovies.di.Container
import com.jbrunton.mymovies.di.ParameterDefinition
import com.jbrunton.mymovies.di.emptyParameterDefinition
import kotlin.reflect.KClass

fun <T : ViewModel> Container.resolveViewModel(
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

fun <T : ViewModel> Container.resolveViewModel(
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