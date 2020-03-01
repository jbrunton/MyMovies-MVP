package com.jbrunton.mymovies.di

import com.jbrunton.mymovies.MyMoviesApplication

class TestApplicationComponent(application: MyMoviesApplication)
    : ApplicationComponent(application)
{
    override fun httpModule() = TestHttpModule
}