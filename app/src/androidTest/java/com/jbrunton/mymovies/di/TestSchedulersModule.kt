package com.jbrunton.mymovies.di

import com.jbrunton.mymovies.entities.SchedulerFactory
import com.jbrunton.mymovies.fixtures.ImmediateSchedulerFactory
import kotlinx.coroutines.test.TestCoroutineContext
import org.koin.dsl.module
import kotlin.coroutines.CoroutineContext

val TestSchedulersModule = module {
    single { TestCoroutineContext() as CoroutineContext }
    single { ImmediateSchedulerFactory() as SchedulerFactory }
}