package com.jbrunton.mymovies.fixtures

import androidx.fragment.app.Fragment
import com.google.android.libraries.cloudtesting.screenshots.ScreenShotter
import com.squareup.spoon.SpoonRule
import org.junit.Rule
import org.koin.test.KoinTest

abstract class BaseFragmentTest<T : Fragment> : KoinTest {
    @get:Rule
    val fragmentRule = createFragmentTestRule()
    @get:Rule
    val spoonRule = SpoonRule()

    val fragment: T get() = fragmentRule.fragment

    @JvmOverloads
    fun takeScreenshot(tag: String = "_") {
        // TODO: figure out why getting permissions errors since upgrading to using API 28
        // spoonRule.screenshot(activityRule.activity, tag)
        ScreenShotter.takeScreenshot(tag, fragmentRule.activity)
    }

    protected abstract fun createFragmentTestRule(): FragmentTestRule<*, T>
}