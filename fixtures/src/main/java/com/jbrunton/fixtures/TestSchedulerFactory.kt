package com.jbrunton.fixtures

import com.jbrunton.entities.SchedulerFactory
import io.reactivex.schedulers.Schedulers
import io.reactivex.schedulers.TestScheduler

class TestSchedulerFactory(testScheduler: TestScheduler) : SchedulerFactory {
    override val Main = Schedulers.trampoline()
    override val IO = testScheduler
}