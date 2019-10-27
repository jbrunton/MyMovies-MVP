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

class LoadingLayoutRobot {
    class Arrange {
        fun replaceDrawables(): Arrange {
            onView(isAssignableFrom(ProgressBar::class.java))
                    .perform(replaceProgressBarDrawable())
            return this
        }
    }

    class Assert {
        fun assertLoadingIndicatorDisplayed(): Assert {
            onView(withId(R.id.loading_indicator))
                    .check(matches(isDisplayed()))
            return this
        }

        fun assertErrorMessage(message: String): Assert {
            onView(withId(R.id.error_text))
                    .check(matches(withText(message)))
            return this
        }

        fun assertTryAgainDisplayed(): Assert {
            onView(withId(R.id.error_try_again))
                    .check(matches(isDisplayed()))
            return this
        }
    }

    fun arrange(block: Arrange.() -> Unit) = Arrange().apply(block)
    fun act(block: () -> Unit) = block()
    fun assert(block: Assert.() -> Unit) = Assert().apply(block)

    companion object {
        fun loadingLayout(block: LoadingLayoutRobot.() -> Unit) = LoadingLayoutRobot().apply(block)
    }
}