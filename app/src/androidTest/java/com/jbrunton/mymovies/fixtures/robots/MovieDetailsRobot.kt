package com.jbrunton.mymovies.fixtures.robots

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import com.jbrunton.mymovies.R
import com.jbrunton.mymovies.fixtures.robots.MovieDetailsRobot.Matchers.MOVIE_DETAILS
import com.jbrunton.mymovies.fixtures.robots.MovieDetailsRobot.Matchers.OVERVIEW

object MovieDetailsRobot {
    object Matchers {
        val MOVIE_DETAILS = withId(R.id.movie_details)
        val OVERVIEW = withId(R.id.overview)
    }

    fun assertDetailsDisplayed() {
        onView(MOVIE_DETAILS).check(matches(isDisplayed()))
    }

    fun assertOverview(overview: String) {
        onView(OVERVIEW).check(matches(withText(overview)))
    }
}
