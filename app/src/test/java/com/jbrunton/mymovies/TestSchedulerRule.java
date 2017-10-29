package com.jbrunton.mymovies;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.schedulers.TestScheduler;

public class TestSchedulerRule implements TestRule {
    public final TestScheduler TEST_SCHEDULER = new TestScheduler();

    @Override public Statement apply(Statement base, Description description) {
        return new Statement() {
            @Override public void evaluate() throws Throwable {
                RxJavaPlugins.setIoSchedulerHandler(
                        scheduler -> TEST_SCHEDULER);
                RxJavaPlugins.setComputationSchedulerHandler(
                        scheduler -> TEST_SCHEDULER);
                RxJavaPlugins.setNewThreadSchedulerHandler(
                        scheduler -> TEST_SCHEDULER);
                RxAndroidPlugins.setInitMainThreadSchedulerHandler(
                        scheduler -> Schedulers.trampoline());

                try {
                    base.evaluate();
                } finally {
                    RxJavaPlugins.reset();
                    RxAndroidPlugins.reset();
                }
            }
        };
    }
}
