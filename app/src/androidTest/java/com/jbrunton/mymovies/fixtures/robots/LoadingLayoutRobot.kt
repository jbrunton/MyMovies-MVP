package com.jbrunton.mymovies.fixtures.robots

import android.widget.ProgressBar
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import com.jbrunton.mymovies.R
import com.jbrunton.mymovies.fixtures.ProgressBarViewActions
import com.jbrunton.mymovies.fixtures.ProgressBarViewActions.replaceProgressBarDrawable

open class LoadingLayoutRobot {
    fun replaceDrawables(): LoadingLayoutRobot {
        onView(isAssignableFrom(ProgressBar::class.java))
                .perform(replaceProgressBarDrawable())
        return this
    }

    fun assertLoadingIndicatorDisplayed(): LoadingLayoutRobot {
        onView(withId(R.id.loading_indicator))
                .check(matches(isDisplayed()))
        return this
    }

    fun assertErrorMessage(message: String): LoadingLayoutRobot {
        onView(withId(R.id.error_text))
                .check(matches(withText(message)))
        return this
    }

    fun assertTryAgainDisplayed(): LoadingLayoutRobot {
        onView(withId(R.id.error_try_again))
                .check(matches(isDisplayed()))
        return this
    }

    companion object {
        fun loadingLayout(block: LoadingLayoutRobot.() -> Unit) = LoadingLayoutRobot().apply(block)
    }
}