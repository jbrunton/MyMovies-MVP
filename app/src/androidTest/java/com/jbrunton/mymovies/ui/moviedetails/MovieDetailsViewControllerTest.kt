package com.jbrunton.mymovies.ui.moviedetails

import android.widget.ProgressBar
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import com.jbrunton.async.AsyncResult
import com.jbrunton.entities.repositories.MoviesRepository
import com.jbrunton.fixtures.MovieFactory
import com.jbrunton.inject.inject
import com.jbrunton.mymovies.R
import com.jbrunton.mymovies.fixtures.*
import com.jbrunton.mymovies.ui.movies.MovieViewState
import com.jbrunton.mymovies.ui.shared.LoadingViewState
import com.jbrunton.mymovies.ui.shared.LoadingViewStateError
import com.jbrunton.mymovies.ui.shared.toLoadingViewState
import org.junit.Before
import org.junit.Test

class MovieDetailsViewControllerTest : BaseFragmentTest<MovieDetailsViewControllerTest.TestFragment>() {
    val LOADING_STATE = AsyncResult.Loading<MovieViewState>()

    val movies = MovieFactory()
    val movie1 = movies.create()
    val moviesRepository: MoviesRepository by inject()

    private val NETWORK_ERROR = LoadingViewStateError("Network Error", R.drawable.ic_error_outline_black_24dp, true)

    @Before
    fun setUp() {
        moviesRepository.stubWith(listOf(movie1))
    }

    @Test
    fun showsLoadingState() {
        onView(ViewMatchers.isAssignableFrom(ProgressBar::class.java)).perform(ProgressBarViewActions.replaceProgressBarDrawable())

        setViewState(LOADING_STATE.toLoadingViewState(MovieViewState.Empty))

        takeScreenshot("showsLoadingState")
        onView(ViewMatchers.withId(R.id.loading_indicator))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun showsErrorState() {
        setViewState(AsyncResult.Failure<MovieViewState>(NETWORK_ERROR).toLoadingViewState(MovieViewState.Empty))

        takeScreenshot("showsErrorState")
        onView(withId(R.id.error_text))
                .check(matches(withText(NETWORK_ERROR.message)))
        onView(withId(R.id.error_try_again))
                .check(matches(isDisplayed()))
    }

    @Test
    fun showsMovieDetails() {
        val viewState = MovieViewState.from(movie1, false)
        setViewState(AsyncResult.Success(viewState).toLoadingViewState(MovieViewState.Empty))

        takeScreenshot()

        onView(withId(R.id.content)).check(matches(isDisplayed()))
        onView(withId(R.id.overview)).check(matches(withText(viewState.overview)))
    }

    override fun createFragmentTestRule(): FragmentTestRule<*, MovieDetailsViewControllerTest.TestFragment> {
        return FragmentTestRule.create(MovieDetailsViewControllerTest.TestFragment::class.java)
    }

    private fun setViewState(viewState: LoadingViewState<MovieViewState>) {
        fragmentRule.runOnUiThread { fragment.updateView(viewState) }
    }

    class TestFragment: ViewControllerTestFragment<LoadingViewState<MovieViewState>>() {
        override fun createViewController() = MovieDetailsViewController()
    }
}