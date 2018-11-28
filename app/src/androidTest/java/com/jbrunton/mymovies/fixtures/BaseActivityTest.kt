package com.jbrunton.mymovies.fixtures

import android.app.Activity
import androidx.test.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule

import com.google.android.libraries.cloudtesting.screenshots.ScreenShotter
import com.jbrunton.inject.Container
import com.jbrunton.inject.HasContainer
import com.squareup.spoon.SpoonRule

import org.junit.Rule

abstract class BaseActivityTest<T : Activity> : HasContainer {
    @get:Rule
    val activityRule = createActivityTestRule()
    @get:Rule
    val spoonRule = SpoonRule()

    val activity: T
        get() = activityRule.activity

    override val container: Container by lazy {
        (activityRule.activity as? HasContainer)?.container
                ?: (InstrumentationRegistry.getInstrumentation().getTargetContext().getApplicationContext() as HasContainer).container
    }

    @JvmOverloads
    fun takeScreenshot(tag: String = "_") {
        // TODO: figure out why getting permissions errors since upgrading to using API 28
        // spoonRule.screenshot(activityRule.activity, tag)
        ScreenShotter.takeScreenshot(tag, activityRule.activity)
    }

    protected abstract fun createActivityTestRule(): ActivityTestRule<T>
}
