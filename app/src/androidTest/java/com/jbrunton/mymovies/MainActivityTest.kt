package com.jbrunton.mymovies

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.jbrunton.mymovies.fixtures.BaseActivityTest
import com.jbrunton.mymovies.ui.main.MainActivity
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class MainActivityTest : BaseActivityTest<MainActivity>() {
    @Test
    fun defaultsToSearchFragment() {
        takeScreenshot()
        onView(withId(R.id.search_query)).check(matches(isDisplayed()))
    }

    override fun createActivityTestRule(): ActivityTestRule<MainActivity> {
        return ActivityTestRule(MainActivity::class.java)
    }
}
