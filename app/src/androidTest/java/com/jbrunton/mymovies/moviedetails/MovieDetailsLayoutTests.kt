package com.jbrunton.mymovies.moviedetails

import android.content.Intent
import android.widget.ProgressBar
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.rule.ActivityTestRule
import com.jbrunton.entities.models.LoadingState
import com.jbrunton.entities.repositories.MoviesRepository
import com.jbrunton.fixtures.MovieFactory
import com.jbrunton.mymovies.R
import com.jbrunton.mymovies.fixtures.BaseActivityTest
import com.jbrunton.mymovies.fixtures.ProgressBarViewActions
import com.jbrunton.mymovies.fixtures.stubSearch
import com.jbrunton.mymovies.fixtures.stubWith
import com.jbrunton.mymovies.movies.MovieViewState
import org.junit.Before
import org.junit.Test
import org.koin.standalone.inject

class MovieDetailsLayoutTests : BaseActivityTest<MovieDetailsActivity>() {
    val LOADING_STATE = LoadingState.Loading<MovieViewState>()

    val movies = MovieFactory()
    val movie1 = movies.create()
    val moviesRepository: MoviesRepository by inject()

    @Before
    fun setUp() {
        moviesRepository.stubWith(listOf(movie1))
        val intent = Intent()
        intent.putExtra("MOVIE_ID", movie1.id)
        activityRule.launchActivity(intent)
    }

    @Test
    fun showsLoadingState() {
        onView(ViewMatchers.isAssignableFrom(ProgressBar::class.java)).perform(ProgressBarViewActions.replaceProgressBarDrawable())

        setViewState(LOADING_STATE)

        takeScreenshot("showsLoadingState")
        Espresso.onView(ViewMatchers.withId(R.id.loading_indicator))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    override fun createActivityTestRule(): ActivityTestRule<MovieDetailsActivity> {
        return ActivityTestRule(MovieDetailsActivity::class.java, false, false)
    }

    private fun setViewState(viewState: MovieDetailsViewState) {
        activityRule.runOnUiThread { activity.updateView(viewState) }
    }
}