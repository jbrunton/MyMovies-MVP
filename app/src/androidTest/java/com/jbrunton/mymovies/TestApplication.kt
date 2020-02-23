package com.jbrunton.mymovies

import com.jbrunton.mymovies.di.TestApplicationComponent

class TestApplication : MyMoviesApplication() {
    override fun createApplicationComponent() = TestApplicationComponent(this)
}
