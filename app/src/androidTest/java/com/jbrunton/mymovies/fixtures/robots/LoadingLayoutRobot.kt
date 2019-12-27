package com.jbrunton.mymovies.fixtures.robots

import android.widget.ProgressBar
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import com.jbrunton.mymovies.R
import com.jbrunton.mymovies.fixtures.ProgressBarViewActions.replaceProgressBarDrawable
import com.jbrunton.mymovies.fixtures.robots.LoadingLayoutRobot.Matchers.ERROR_TEXT
import com.jbrunton.mymovies.fixtures.robots.LoadingLayoutRobot.Matchers.ERROR_TRY_AGAIN
import com.jbrunton.mymovies.fixtures.robots.LoadingLayoutRobot.Matchers.LOADING_INDICATOR
import com.jbrunton.mymovies.fixtures.robots.LoadingLayoutRobot.Matchers.PROGRESS_BAR

object LoadingLayoutRobot {
    object Matchers {
        val PROGRESS_BAR = isAssignableFrom(ProgressBar::class.java)
        val LOADING_INDICATOR = withId(R.id.loading_indicator)
        val ERROR_TEXT = withId(R.id.error_text)
        val ERROR_TRY_AGAIN = withId(R.id.error_try_again)
    }

    fun replaceDrawables() {
        onView(PROGRESS_BAR).perform(replaceProgressBarDrawable())
    }

    fun assertLoadingIndicatorDisplayed() {
        onView(LOADING_INDICATOR).check(matches(isDisplayed()))
    }

    fun assertErrorMessage(message: String) {
        onView(ERROR_TEXT).check(matches(withText(message)))
    }

    fun assertTryAgainDisplayed() {
        onView(ERROR_TRY_AGAIN).check(matches(isDisplayed()))
    }
}
