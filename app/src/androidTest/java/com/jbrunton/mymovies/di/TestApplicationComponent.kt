package com.jbrunton.mymovies.di

import com.jbrunton.mymovies.MyMoviesApplication

class TestApplicationComponent(application: MyMoviesApplication)
    : ApplicationComponent(application)
{
    override fun schedulersModule() = TestSchedulersModule
    override fun httpModule() = TestHttpModule
}