package com.jbrunton.mymovies.shared

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import org.koin.core.parameter.emptyParameterDefinition
import kotlin.reflect.KClass

fun <T : ViewModel> Container.viewModel(
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
