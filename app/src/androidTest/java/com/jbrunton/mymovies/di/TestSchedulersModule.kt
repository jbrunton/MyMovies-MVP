package com.jbrunton.mymovies.di

import com.jbrunton.mymovies.entities.SchedulerFactory
import com.jbrunton.mymovies.fixtures.ImmediateSchedulerFactory
import com.jbrunton.inject.module
import kotlinx.coroutines.test.TestCoroutineContext
import kotlin.coroutines.CoroutineContext

val TestSchedulersModule = module {
    single { TestCoroutineContext() as CoroutineContext }
    single { ImmediateSchedulerFactory() as SchedulerFactory }
}