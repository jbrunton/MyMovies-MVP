package com.jbrunton.mymovies

import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.jbrunton.mymovies.di.ApplicationComponent
import com.jbrunton.mymovies.di.TestApplicationComponent
import com.jbrunton.mymovies.fixtures.rules.application
import com.jbrunton.mymovies.ui.main.MainActivity
import com.jbrunton.mymovies.ui.moviedetails.MovieDetailsViewModel
import com.jbrunton.mymovies.usecases.moviedetails.MovieDetailsUseCase
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.android.ext.koin.androidContext
import org.koin.core.parameter.parametersOf
import org.koin.dsl.koinApplication
import org.koin.test.KoinTest
import org.koin.test.check.CheckParameters
import org.koin.test.check.ParametersBinding
import org.koin.test.check.checkModules

@RunWith(AndroidJUnit4::class)
class ContainerTest : KoinTest {
    @get:Rule
    val activityRule = ActivityTestRule(MainActivity::class.java)

    val parameters: CheckParameters = {
        create<MovieDetailsViewModel> { parametersOf("1") }
        create<MovieDetailsUseCase> { parametersOf("1") }
    }

    @Test
    fun checkAppModule() {
        val applicationModules = ApplicationComponent(activityRule.application).createModules()
        koinApplication {
            androidContext(activityRule.application)
            modules(applicationModules)
        }.checkModules(parameters)
    }

    @Test
    fun checkTestAppModule() {
        val applicationModules = TestApplicationComponent(activityRule.application).createModules()
        koinApplication {
            androidContext(activityRule.application)
            modules(applicationModules)
        }.checkModules(parameters)
    }
//
//    @Ignore @Test // ActivityModule is not a complete module - need to allow others to be passed in
//    fun checkActivityModule() {
//        ActivityModule(activityRule.activity).check()
//    }
}
