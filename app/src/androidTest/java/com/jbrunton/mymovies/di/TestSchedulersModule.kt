package com.jbrunton.mymovies.di

import com.jbrunton.mymovies.entities.SchedulerFactory
import com.jbrunton.mymovies.fixtures.ImmediateSchedulerFactory
import kotlinx.coroutines.test.TestCoroutineContext
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton
import kotlin.coroutines.CoroutineContext

val TestSchedulersModule = Kodein.Module("TestSchedulersModule") {
    bind() from singleton { TestCoroutineContext() as CoroutineContext }
    bind() from singleton { ImmediateSchedulerFactory() as SchedulerFactory }
}
