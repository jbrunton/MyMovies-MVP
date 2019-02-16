package com.jbrunton.fixtures

import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.TestScheduler
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

class TestSchedulerRule : TestRule {
    val TEST_SCHEDULER = TestScheduler()

    override fun apply(base: Statement, description: Description): Statement {
        return object : Statement() {
            @Throws(Throwable::class)
            override fun evaluate() {
                RxJavaPlugins.setIoSchedulerHandler { scheduler -> TEST_SCHEDULER }
                RxJavaPlugins.setComputationSchedulerHandler { scheduler -> TEST_SCHEDULER }
                RxJavaPlugins.setNewThreadSchedulerHandler { scheduler -> TEST_SCHEDULER }

                try {
                    base.evaluate()
                } finally {
                    RxJavaPlugins.reset()
                }
            }
        }
    }
}