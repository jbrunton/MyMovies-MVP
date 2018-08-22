package com.jbrunton.mymovies.fixtures

import android.app.Application
import android.content.Context
import android.support.test.runner.AndroidJUnitRunner

import com.jbrunton.mymovies.TestApplication

class TestRunner : AndroidJUnitRunner() {
    @Throws(IllegalAccessException::class, ClassNotFoundException::class, InstantiationException::class)
    override fun newApplication(cl: ClassLoader, className: String, context: Context): Application {
        return super.newApplication(cl, TestApplication::class.java.name, context)
    }
}
