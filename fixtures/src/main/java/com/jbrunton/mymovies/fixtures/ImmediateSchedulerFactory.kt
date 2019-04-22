package com.jbrunton.mymovies.fixtures

import com.jbrunton.mymovies.entities.SchedulerFactory
import io.reactivex.schedulers.Schedulers

class ImmediateSchedulerFactory : SchedulerFactory {
    override val IO = Schedulers.trampoline()
    override val Main = Schedulers.trampoline()
}