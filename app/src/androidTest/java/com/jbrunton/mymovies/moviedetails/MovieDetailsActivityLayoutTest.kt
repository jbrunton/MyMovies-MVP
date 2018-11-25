package com.jbrunton.mymovies.moviedetails

import android.content.Intent
import android.widget.ProgressBar
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.rule.ActivityTestRule
import com.jbrunton.entities.models.Result
import com.jbrunton.entities.repositories.MoviesRepository
import com.jbrunton.fixtures.MovieFactory
import com.jbrunton.mymovies.R
import com.jbrunton.mymovies.fixtures.*
import com.jbrunton.mymovies.movies.MovieViewState
import com.jbrunton.mymovies.shared.LoadingViewStateError
import org.junit.Before
import org.junit.Test
import org.koin.standalone.inject

class MovieDetailsActivityLayoutTest : BaseActivityTest<MovieDetailsActivity>() {
    val LOADING_STATE = Result.Loading<MovieViewState>()

    val movies = MovieFactory()
    val movie1 = movies.create()
    val moviesRepository: MoviesRepository by inject()

    private val NETWORK_ERROR = LoadingViewStateError("Network Error", R.drawable.ic_error_outline_black_24dp, true)


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
        onView(ViewMatchers.withId(R.id.loading_indicator))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun showsErrorState() {
        setViewState(Result.Failure(NETWORK_ERROR))

        takeScreenshot("showsErrorState")
        onView(withId(R.id.error_text))
                .check(matches(withText(NETWORK_ERROR.message)))
        onView(withId(R.id.error_try_again))
                .check(matches(isDisplayed()))
    }

    @Test
    fun showsMovieDetails() {
        val viewState = MovieViewState(movie1)
        setViewState(Result.Success(viewState))

        takeScreenshot()

        onView(withId(R.id.content)).check(matches(isDisplayed()))
        onView(withId(R.id.overview)).check(matches(withText(viewState.overview)))
    }

    override fun createActivityTestRule(): ActivityTestRule<MovieDetailsActivity> {
        return ActivityTestRule(MovieDetailsActivity::class.java, false, false)
    }

    private fun setViewState(viewState: MovieDetailsViewState) {
        activityRule.runOnUiThread { activity.updateView(viewState) }
    }
}