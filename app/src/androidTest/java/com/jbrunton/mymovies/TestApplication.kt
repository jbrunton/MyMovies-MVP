package com.jbrunton.mymovies

import com.jbrunton.mymovies.di.TestHttpModule
import com.jbrunton.mymovies.di.TestSchedulersModule

class TestApplication : MyMoviesApplication() {
    override fun schedulersModule() = TestSchedulersModule()
    override fun httpModule() = TestHttpModule()
}
