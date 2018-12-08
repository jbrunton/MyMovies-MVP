package com.jbrunton.mymovies.di

import com.jbrunton.inject.module
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

val SchedulersModule = module {
    single { Schedulers.computation() }
    single { Dispatchers.Main as CoroutineContext }
}
