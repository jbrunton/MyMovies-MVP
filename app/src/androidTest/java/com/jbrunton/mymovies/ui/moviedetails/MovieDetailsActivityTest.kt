package com.jbrunton.mymovies.ui.moviedetails

import android.content.Intent
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.jbrunton.entities.repositories.MoviesRepository
import com.jbrunton.fixtures.MovieFactory
import com.jbrunton.mymovies.R
import com.jbrunton.inject.inject
import com.jbrunton.mymovies.fixtures.BaseActivityTest
import com.jbrunton.mymovies.fixtures.stubWith
import com.jbrunton.mymovies.ui.movies.MovieViewState
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MovieDetailsActivityTest : BaseActivityTest<MovieDetailsActivity>() {
    val MOVIE_FACTORY = MovieFactory()
    val MOVIE1 = MOVIE_FACTORY.create()

    val moviesRepository: MoviesRepository by inject()

    @Test
    fun loadsMovieDetails() {
        moviesRepository.stubWith(listOf(MOVIE1))
        val expectedViewState = MovieViewState.from(MOVIE1, false)

        val intent = Intent()
        intent.putExtra("MOVIE_ID", MOVIE1.id)
        activityRule.launchActivity(intent)

        takeScreenshot()
        onView(withId(R.id.movie_details)).check(matches(ViewMatchers.isDisplayed()))
        onView(withId(R.id.overview)).check(matches(withText(expectedViewState.overview)))
    }

    override fun createActivityTestRule(): ActivityTestRule<MovieDetailsActivity> {
        return ActivityTestRule(MovieDetailsActivity::class.java, false, false)
    }
}