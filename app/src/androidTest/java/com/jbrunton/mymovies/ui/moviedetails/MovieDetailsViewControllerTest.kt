package com.jbrunton.mymovies.ui.moviedetails

import android.widget.ProgressBar
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import com.jbrunton.async.AsyncResult
import com.jbrunton.mymovies.R
import com.jbrunton.mymovies.fixtures.MovieFactory
import com.jbrunton.mymovies.fixtures.ProgressBarViewActions
import com.jbrunton.mymovies.fixtures.robots.LoadingLayoutRobot
import com.jbrunton.mymovies.fixtures.robots.LoadingLayoutRobot.Companion.loadingLayout
import com.jbrunton.mymovies.fixtures.rules.ViewControllerTestRule
import com.jbrunton.mymovies.fixtures.rules.takeScreenshot
import com.jbrunton.mymovies.libs.ui.viewstates.LoadingViewStateError
import com.jbrunton.mymovies.libs.ui.viewstates.toLoadingViewState
import org.junit.Before
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
    fun showsLoadingState() {
        loadingLayout {
            arrange {
                replaceDrawables()
            }
            act {
                controllerRule.setViewState(LOADING_STATE.toLoadingViewState(MovieDetailsViewState.Empty))
            }
            assert {
                controllerRule.takeScreenshot("showsLoadingState")
                assertLoadingIndicatorDisplayed()
            }
        }
    }

    @Test
    fun showsErrorState() {
        loadingLayout {
            act {
                controllerRule.setViewState(AsyncResult.Failure<MovieDetailsViewState>(NETWORK_ERROR).toLoadingViewState(MovieDetailsViewState.Empty))
            }
            assert {
                controllerRule.takeScreenshot("showsErrorState")
                assertErrorMessage(NETWORK_ERROR.message)
                assertTryAgainDisplayed()
            }
        }
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