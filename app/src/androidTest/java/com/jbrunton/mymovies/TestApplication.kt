package com.jbrunton.mymovies

import com.jbrunton.inject.Container
import com.jbrunton.mymovies.di.TestApplicationComponent

class TestApplication : MyMoviesApplication() {
    override fun configureContainer(container: Container) {
        container.register(TestApplicationComponent(this))
    }
}
