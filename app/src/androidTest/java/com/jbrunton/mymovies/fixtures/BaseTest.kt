package com.jbrunton.mymovies.fixtures

import android.app.Activity
import androidx.test.rule.ActivityTestRule

import com.google.android.libraries.cloudtesting.screenshots.ScreenShotter
import com.squareup.spoon.SpoonRule

import org.junit.Rule

abstract class BaseTest<T : Activity> {
    @get:Rule
    val activityRule = createActivityTestRule()
    @get:Rule
    val spoonRule = SpoonRule()

    @JvmOverloads
    fun takeScreenshot(tag: String = "_") {
        spoonRule.screenshot(activityRule.activity, tag)
        ScreenShotter.takeScreenshot(tag, activityRule.activity)
    }

    protected abstract fun createActivityTestRule(): ActivityTestRule<T>
}
