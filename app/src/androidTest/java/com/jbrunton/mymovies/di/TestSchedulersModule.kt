package com.jbrunton.mymovies.di

import io.reactivex.schedulers.Schedulers

val TestSchedulersModule = module {
    single { Schedulers.trampoline() }
}
