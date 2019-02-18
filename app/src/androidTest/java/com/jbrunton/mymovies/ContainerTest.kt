package com.jbrunton.mymovies

import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.jbrunton.inject.DryRunParameters
import com.jbrunton.inject.check
import com.jbrunton.inject.parametersOf
import com.jbrunton.mymovies.di.ActivityModule
import com.jbrunton.mymovies.di.ApplicationComponent
import com.jbrunton.mymovies.di.TestApplicationComponent
import com.jbrunton.mymovies.fixtures.BaseActivityTest
import com.jbrunton.mymovies.ui.discover.GenreResultsViewModel
import com.jbrunton.mymovies.ui.main.MainActivity
import com.jbrunton.mymovies.ui.moviedetails.MovieDetailsViewModel
import com.jbrunton.mymovies.usecases.moviedetails.MovieDetailsUseCase
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ContainerTest : BaseActivityTest<MainActivity>() {
    val parameters = DryRunParameters().apply {
        paramsFor(MovieDetailsViewModel::class, parametersOf("1"))
        paramsFor(GenreResultsViewModel::class, parametersOf("1"))
        paramsFor(MovieDetailsUseCase::class, parametersOf("1"))
    }

    @Test
    fun checkAppModule() {
        ApplicationComponent(application).check(parameters)
    }

    @Test
    fun checkTestAppModule() {
        TestApplicationComponent(application).check(parameters)
    }

    @Ignore @Test // ActivityModule is not a complete module - need to allow others to be passed in
    fun checkActivityModule() {
        ActivityModule(activity).check()
    }

    override fun createActivityTestRule(): ActivityTestRule<MainActivity> {
        return ActivityTestRule(MainActivity::class.java)
    }

    private val application: MyMoviesApplication
        get() = activity.application as MyMoviesApplication
}