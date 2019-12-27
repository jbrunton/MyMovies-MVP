package com.jbrunton.mymovies.ui.moviedetails

import com.jbrunton.async.AsyncResult
import com.jbrunton.mymovies.R
import com.jbrunton.mymovies.fixtures.MovieFactory
import com.jbrunton.mymovies.fixtures.robots.LoadingLayoutRobot.assertErrorMessage
import com.jbrunton.mymovies.fixtures.robots.LoadingLayoutRobot.assertLoadingIndicatorDisplayed
import com.jbrunton.mymovies.fixtures.robots.LoadingLayoutRobot.assertTryAgainDisplayed
import com.jbrunton.mymovies.fixtures.robots.LoadingLayoutRobot.replaceDrawables
import com.jbrunton.mymovies.fixtures.robots.MovieDetailsRobot.assertDetailsDisplayed
import com.jbrunton.mymovies.fixtures.robots.MovieDetailsRobot.assertOverview
import com.jbrunton.mymovies.fixtures.rules.ViewControllerTestRule
import com.jbrunton.mymovies.fixtures.rules.takeScreenshot
import com.jbrunton.mymovies.libs.ui.viewstates.LoadingViewStateError
import com.jbrunton.mymovies.libs.ui.viewstates.toLoadingViewState
import org.junit.Rule
import org.junit.Test

class MovieDetailsViewControllerTest {
    @get:Rule
    val controllerRule = ViewControllerTestRule.create(MovieDetailsViewController(), R.layout.activity_movie_details)

    val LOADING_STATE = AsyncResult.Loading<MovieDetailsViewState>()

    val movies = MovieFactory()
    val movie1 = movies.create()

    private val NETWORK_ERROR = LoadingViewStateError("Network Error", R.drawable.ic_error_outline_black_24dp, true)

    @Test
    fun showsLoadingState()  {
        replaceDrawables()

        controllerRule.setViewState(LOADING_STATE.toLoadingViewState(MovieDetailsViewState.Empty))

        controllerRule.takeScreenshot("showsLoadingState")
        assertLoadingIndicatorDisplayed()
    }

    @Test
    fun showsErrorState() {
        controllerRule.setViewState(AsyncResult.Failure<MovieDetailsViewState>(NETWORK_ERROR).toLoadingViewState(MovieDetailsViewState.Empty))

        controllerRule.takeScreenshot("showsErrorState")
        assertErrorMessage(NETWORK_ERROR.message)
        assertTryAgainDisplayed()
    }

    @Test
    fun showsMovieDetails() {
        val viewState = MovieDetailsViewState.from(movie1, false)
        controllerRule.setViewState(AsyncResult.Success(viewState).toLoadingViewState(MovieDetailsViewState.Empty))

        controllerRule.takeScreenshot()
        assertDetailsDisplayed()
        assertOverview(viewState.overview)
    }
}