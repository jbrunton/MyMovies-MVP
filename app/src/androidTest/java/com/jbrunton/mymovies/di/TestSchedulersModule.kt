package com.jbrunton.mymovies.di

import com.jbrunton.inject.module
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.test.TestCoroutineContext
import kotlin.coroutines.CoroutineContext

val TestSchedulersModule = module {
    single { Schedulers.trampoline() }
    single { TestCoroutineContext() as CoroutineContext }
}