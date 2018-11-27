package com.jbrunton.mymovies.fixtures

import android.app.Activity
import androidx.test.rule.ActivityTestRule

import com.google.android.libraries.cloudtesting.screenshots.ScreenShotter
import com.jbrunton.mymovies.di.Container
import com.jbrunton.mymovies.di.HasContainer
import com.jbrunton.mymovies.shared.BaseActivity
import com.squareup.spoon.SpoonRule

import org.junit.Rule

abstract class BaseActivityTest<T : Activity> : HasContainer {
    @get:Rule
    val activityRule = createActivityTestRule()
    @get:Rule
    val spoonRule = SpoonRule()

    val activity: T
        get() = activityRule.activity

    override val container: Container
        get() = (activity as BaseActivity<*>).container

    @JvmOverloads
    fun takeScreenshot(tag: String = "_") {
        // TODO: figure out why getting permissions errors since upgrading to using API 28
        // spoonRule.screenshot(activityRule.activity, tag)
        ScreenShotter.takeScreenshot(tag, activityRule.activity)
    }

    protected abstract fun createActivityTestRule(): ActivityTestRule<T>
}
