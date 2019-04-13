package com.jbrunton.mymovies

import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.jbrunton.inject.*
import com.jbrunton.mymovies.di.ActivityModule
import com.jbrunton.mymovies.di.ApplicationComponent
import com.jbrunton.mymovies.di.TestApplicationComponent
import com.jbrunton.mymovies.fixtures.rules.application
import com.jbrunton.mymovies.fixtures.rules.container
import com.jbrunton.mymovies.ui.main.MainActivity
import com.jbrunton.mymovies.ui.moviedetails.MovieDetailsViewModel
import com.jbrunton.mymovies.usecases.moviedetails.MovieDetailsUseCase
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ContainerTest : HasContainer {
    @get:Rule
    val activityRule = ActivityTestRule(MainActivity::class.java)

    override val container: Container
        get() = activityRule.container

    val parameters = DryRunParameters().apply {
        paramsFor(MovieDetailsViewModel::class, parametersOf("1"))
        paramsFor(MovieDetailsUseCase::class, parametersOf("1"))
    }

    @Test
    fun checkAppModule() {
        ApplicationComponent(activityRule.application).check(parameters)
    }

    @Test
    fun checkTestAppModule() {
        TestApplicationComponent(activityRule.application).check(parameters)
    }

    @Ignore @Test // ActivityModule is not a complete module - need to allow others to be passed in
    fun checkActivityModule() {
        ActivityModule(activityRule.activity).check()
    }
}