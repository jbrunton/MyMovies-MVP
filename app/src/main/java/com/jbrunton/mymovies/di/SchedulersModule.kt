package com.jbrunton.mymovies.di

import com.jbrunton.entities.SchedulerFactory
import com.jbrunton.inject.module
import com.jbrunton.mymovies.ui.shared.AndroidSchedulerFactory
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

val SchedulersModule = module {
    single { Dispatchers.Main as CoroutineContext }
    single { AndroidSchedulerFactory() as SchedulerFactory }
}
