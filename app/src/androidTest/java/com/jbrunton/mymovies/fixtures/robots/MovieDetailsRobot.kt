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

class MovieDetailsRobot : LoadingLayoutRobot() {
    fun assertDetailsDisplayed(): MovieDetailsRobot {
        onView(withId(R.id.movie_details)).check(matches(isDisplayed()))
        return this
    }

    fun assertOverview(overview: String): MovieDetailsRobot {
        onView(withId(R.id.overview)).check(matches(withText(overview)))
        return this
    }

    companion object {
        fun movieDetails(block: MovieDetailsRobot.() -> Unit) = MovieDetailsRobot().apply(block)
    }
}