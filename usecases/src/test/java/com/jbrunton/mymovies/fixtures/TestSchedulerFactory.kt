package com.jbrunton.mymovies.fixtures

import com.jbrunton.mymovies.usecases.SchedulerFactory
import io.reactivex.schedulers.Schedulers
import io.reactivex.schedulers.TestScheduler

class TestSchedulerFactory(val testScheduler: TestScheduler) : SchedulerFactory(
        Main = Schedulers.trampoline(),
        IO = testScheduler
)
