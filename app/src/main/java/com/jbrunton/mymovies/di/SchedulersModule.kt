package com.jbrunton.mymovies.di

import com.jbrunton.inject.module
import io.reactivex.schedulers.Schedulers

val SchedulersModule = module {
    single { Schedulers.computation() }
}
