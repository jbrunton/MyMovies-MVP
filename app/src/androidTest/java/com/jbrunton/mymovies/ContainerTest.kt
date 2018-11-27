package com.jbrunton.mymovies

import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.jbrunton.mymovies.di.*
import com.jbrunton.mymovies.fixtures.BaseActivityTest
import com.jbrunton.mymovies.ui.discover.GenreResultsViewModel
import com.jbrunton.mymovies.ui.main.MainActivity
import com.jbrunton.mymovies.ui.moviedetails.MovieDetailsViewModel
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.reflect.KClass

@RunWith(AndroidJUnit4::class)
class ContainerTest : BaseActivityTest<MainActivity>() {
    val parameters = DryRunParameters().apply {
        map(MovieDetailsViewModel::class, parametersOf("1"))
        map(GenreResultsViewModel::class, parametersOf("1"))
    }

    @Test
    fun checkAppModule() {
        AppModule().check(parameters)
    }

    @Test
    fun checkTestAppModule() {
        TestAppModule().check(parameters)
    }

    @Test
    fun checkActivityModule() {
        ActivityModule(activity).check()
    }

    override fun createActivityTestRule(): ActivityTestRule<MainActivity> {
        return ActivityTestRule(MainActivity::class.java)
    }
}