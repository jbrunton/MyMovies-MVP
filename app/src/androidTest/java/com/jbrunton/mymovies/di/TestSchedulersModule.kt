package com.jbrunton.mymovies.di

import io.reactivex.schedulers.Schedulers

class TestSchedulersModule : Module {
    override fun registerTypes(container: Container) {
        container.apply {
            single { Schedulers.trampoline() }
        }
    }
}