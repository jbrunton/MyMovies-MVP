package com.jbrunton.mymovies.di

import io.reactivex.schedulers.Schedulers

val SchedulersModule = module {
    single { Schedulers.computation() }
}
