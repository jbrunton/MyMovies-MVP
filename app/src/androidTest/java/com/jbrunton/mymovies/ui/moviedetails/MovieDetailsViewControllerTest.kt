package com.jbrunton.mymovies.ui.moviedetails

import android.widget.ProgressBar
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import com.jbrunton.async.AsyncResult
import com.jbrunton.fixtures.MovieFactory
import com.jbrunton.mymovies.R
import com.jbrunton.mymovies.fixtures.ProgressBarViewActions
import com.jbrunton.mymovies.fixtures.ViewControllerTestRule
import com.jbrunton.mymovies.fixtures.takeScreenshot
import com.jbrunton.mymovies.ui.shared.LoadingViewStateError
import com.jbrunton.mymovies.ui.shared.toLoadingViewState
import org.junit.Rule
import org.junit.Test

class MovieDetailsViewControllerTest {
    @get:Rule
    val controllerRule = ViewControllerTestRule.create(MovieDetailsViewController())

    val LOADING_STATE = AsyncResult.Loading<MovieDetailsViewState>()

    val movies = MovieFactory()
    val movie1 = movies.create()

    private val NETWORK_ERROR = LoadingViewStateError("Network Error", R.drawable.ic_error_outline_black_24dp, true)

    @Test
    fun showsLoadingState() {
        onView(ViewMatchers.isAssignableFrom(ProgressBar::class.java)).perform(ProgressBarViewActions.replaceProgressBarDrawable())
        controllerRule.setViewState(LOADING_STATE.toLoadingViewState(MovieDetailsViewState.Empty))

        controllerRule.takeScreenshot("showsLoadingState")
        onView(ViewMatchers.withId(R.id.loading_indicator))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun showsErrorState() {
        controllerRule.setViewState(AsyncResult.Failure<MovieDetailsViewState>(NETWORK_ERROR).toLoadingViewState(MovieDetailsViewState.Empty))

        controllerRule.takeScreenshot("showsErrorState")
        onView(withId(R.id.error_text))
                .check(matches(withText(NETWORK_ERROR.message)))
        onView(withId(R.id.error_try_again))
                .check(matches(isDisplayed()))
    }

    @Test
    fun showsMovieDetails() {
        val viewState = MovieDetailsViewState.from(movie1, false)
        controllerRule.setViewState(AsyncResult.Success(viewState).toLoadingViewState(MovieDetailsViewState.Empty))

        controllerRule.takeScreenshot()

        onView(withId(R.id.movie_details)).check(matches(isDisplayed()))
        onView(withId(R.id.overview)).check(matches(withText(viewState.overview)))
    }
}