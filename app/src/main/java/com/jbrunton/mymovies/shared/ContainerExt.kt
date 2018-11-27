package com.jbrunton.mymovies.shared

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import kotlin.reflect.KClass

fun <T : ViewModel> Container.viewModel(
        activity: FragmentActivity,
        klass: KClass<T>
): T {
    return ViewModelProviders.of(activity, object : ViewModelProvider.Factory {
        override fun <S : ViewModel> create(modelClass: Class<S>): S {
            return resolve(klass) as S
        }
    }).get(klass.java)
}
