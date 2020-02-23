package com.jbrunton.mymovies.di

import com.jbrunton.mymovies.entities.SchedulerFactory
import com.jbrunton.mymovies.libs.ui.AndroidSchedulerFactory
import kotlinx.coroutines.Dispatchers
import org.kodein.di.Kodein.Module
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton
import kotlin.coroutines.CoroutineContext

val SchedulersModule = Module("SchedulersModule") {
    bind() from singleton { Dispatchers.Main as CoroutineContext }
    bind() from singleton { AndroidSchedulerFactory() as SchedulerFactory }
}
