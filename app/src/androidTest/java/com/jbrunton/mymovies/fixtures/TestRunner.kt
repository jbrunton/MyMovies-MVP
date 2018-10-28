package com.jbrunton.mymovies.fixtures

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner

import com.jbrunton.mymovies.TestApplication
import com.squareup.rx2.idler.Rx2Idler
import io.reactivex.plugins.RxJavaPlugins

class TestRunner : AndroidJUnitRunner() {
    @Throws(IllegalAccessException::class, ClassNotFoundException::class, InstantiationException::class)
    override fun newApplication(cl: ClassLoader, className: String, context: Context): Application {
        return super.newApplication(cl, TestApplication::class.java.name, context)
    }

    override fun onStart() {
        RxJavaPlugins.setInitComputationSchedulerHandler(Rx2Idler.create("Computation Scheduler"));
        RxJavaPlugins.setInitIoSchedulerHandler(Rx2Idler.create("IO Scheduler"));
        RxJavaPlugins.setInitNewThreadSchedulerHandler(Rx2Idler.create("New thread Scheduler"));
        RxJavaPlugins.setInitSingleSchedulerHandler(Rx2Idler.create("Single scheduler"));

        super.onStart()
    }
}
