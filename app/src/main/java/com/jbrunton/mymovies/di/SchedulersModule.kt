package com.jbrunton.mymovies.di

import com.jbrunton.entities.SchedulerFactory
import com.jbrunton.inject.module
import com.jbrunton.mymovies.ui.shared.AndroidSchedulerFactory
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

val SchedulersModule = module {
    single { Schedulers.computation() }
    single { Dispatchers.Main as CoroutineContext }
    single { AndroidSchedulerFactory() as SchedulerFactory }
}
