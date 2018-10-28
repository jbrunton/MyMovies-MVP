package com.jbrunton.mymovies.fixtures

import android.app.Activity
import androidx.test.rule.ActivityTestRule

import com.google.android.libraries.cloudtesting.screenshots.ScreenShotter
import com.squareup.spoon.SpoonRule

import org.junit.Rule
import org.koin.test.KoinTest

abstract class BaseTest<T : Activity> : KoinTest {
    @get:Rule
    val activityRule = createActivityTestRule()
    @get:Rule
    val spoonRule = SpoonRule()

    @JvmOverloads
    fun takeScreenshot(tag: String = "_") {
        // TODO: figure out why getting permissions errors since upgrading to using API 28
        // spoonRule.screenshot(activityRule.activity, tag)
        ScreenShotter.takeScreenshot(tag, activityRule.activity)
    }

    protected abstract fun createActivityTestRule(): ActivityTestRule<T>
}
