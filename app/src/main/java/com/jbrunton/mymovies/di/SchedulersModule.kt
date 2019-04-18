package com.jbrunton.mymovies.di

import com.jbrunton.entities.SchedulerFactory
import com.jbrunton.inject.module
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

val SchedulersModule = module {
    single { Dispatchers.Main as CoroutineContext }
    single { com.jbrunton.libs.ui.AndroidSchedulerFactory() as SchedulerFactory }
}
