package com.jbrunton.mymovies.fixtures

import com.jbrunton.mymovies.usecases.SchedulerFactory
import io.reactivex.schedulers.Schedulers

class InstantSchedulerFactory : SchedulerFactory(
        Main = Schedulers.trampoline(),
        IO = Schedulers.trampoline()
)
