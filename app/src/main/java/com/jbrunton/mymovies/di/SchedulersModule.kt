package com.jbrunton.mymovies.di

import com.jbrunton.inject.module
import com.jbrunton.mymovies.usecases.SchedulerFactory
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

val SchedulersModule = module {
    single { Schedulers.computation() }
    single { Dispatchers.Main as CoroutineContext }
    single { SchedulerFactory(AndroidSchedulers.mainThread(), Schedulers.io()) }
}

