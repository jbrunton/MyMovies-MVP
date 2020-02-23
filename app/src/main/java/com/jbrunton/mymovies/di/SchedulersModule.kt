package com.jbrunton.mymovies.di

import com.jbrunton.mymovies.entities.SchedulerFactory
import com.jbrunton.inject.module
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

val SchedulersModule = module {
    single { Dispatchers.Main as CoroutineContext }
    single { com.jbrunton.mymovies.libs.ui.AndroidSchedulerFactory() as SchedulerFactory }
}

val KoinSchedulersModule = org.koin.dsl.module {
    single { Dispatchers.Main as CoroutineContext }
    single { com.jbrunton.mymovies.libs.ui.AndroidSchedulerFactory() as SchedulerFactory }
}
