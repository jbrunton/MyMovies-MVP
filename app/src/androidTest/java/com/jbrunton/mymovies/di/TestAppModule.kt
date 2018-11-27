package com.jbrunton.mymovies.di

class TestAppModule : AppModule() {
    override fun schedulersModule() = TestSchedulersModule()
    override fun httpModule() = TestHttpModule()
}