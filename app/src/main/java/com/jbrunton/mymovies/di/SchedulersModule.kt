package com.jbrunton.mymovies.di

import com.jbrunton.mymovies.entities.SchedulerFactory
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

val KoinSchedulersModule = org.koin.dsl.module {
    single { Dispatchers.Main as CoroutineContext }
    single { com.jbrunton.mymovies.libs.ui.AndroidSchedulerFactory() as SchedulerFactory }
}
