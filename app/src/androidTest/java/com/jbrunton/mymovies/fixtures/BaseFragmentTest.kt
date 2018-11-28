package com.jbrunton.mymovies.fixtures

import androidx.fragment.app.Fragment
import androidx.test.InstrumentationRegistry.getInstrumentation
import com.google.android.libraries.cloudtesting.screenshots.ScreenShotter
import com.jbrunton.inject.Container
import com.jbrunton.inject.HasContainer
import com.squareup.spoon.SpoonRule
import org.junit.Rule

abstract class BaseFragmentTest<T : Fragment> : HasContainer {
    @get:Rule
    val fragmentRule = createFragmentTestRule()
    @get:Rule
    val spoonRule = SpoonRule()

    val fragment: T get() = fragmentRule.fragment

    override val container: Container by lazy {
        (fragment as? HasContainer)?.container
                ?: (getInstrumentation().getTargetContext().getApplicationContext() as HasContainer).container
    }


    @JvmOverloads
    fun takeScreenshot(tag: String = "_") {
        // TODO: figure out why getting permissions errors since upgrading to using API 28
        // spoonRule.screenshot(activityRule.activity, tag)
        ScreenShotter.takeScreenshot(tag, fragmentRule.activity)
    }

    protected abstract fun createFragmentTestRule(): FragmentTestRule<*, T>
}