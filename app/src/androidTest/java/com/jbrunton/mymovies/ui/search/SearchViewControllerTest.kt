package com.jbrunton.mymovies.ui.search

import android.widget.ProgressBar
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.runner.AndroidJUnit4
import com.jbrunton.async.AsyncResult
import com.jbrunton.mymovies.R
import com.jbrunton.mymovies.entities.models.Movie
import com.jbrunton.mymovies.features.search.SearchViewStateFactory
import com.jbrunton.mymovies.fixtures.MovieFactory
import com.jbrunton.mymovies.fixtures.ProgressBarViewActions
import com.jbrunton.mymovies.fixtures.RecyclerViewUtils.withRecyclerView
import com.jbrunton.mymovies.fixtures.rules.ViewControllerTestRule
import com.jbrunton.mymovies.fixtures.rules.takeScreenshot
import com.jbrunton.mymovies.libs.ui.viewstates.LoadingViewState
import com.jbrunton.mymovies.libs.ui.viewstates.LoadingViewStateError
import com.jbrunton.mymovies.shared.ui.MoviesListViewController
import com.jbrunton.mymovies.shared.ui.SearchViewState
import com.jbrunton.mymovies.usecases.search.SearchResult
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class SearchViewControllerTest {
    @get:Rule
    val controllerRule = ViewControllerTestRule.create(object : MoviesListViewController() {
        override val contentView: RecyclerView get() = view.findViewById(R.id.movies_list)
        override fun onMovieSelected(movie: Movie) {}
    })

    val MOVIE_FACTORY = MovieFactory()
    val MOVIE1 = MOVIE_FACTORY.create()
    val MOVIE2 = MOVIE_FACTORY.create()

    lateinit var viewStateFactory: SearchViewStateFactory

    lateinit var EMPTY_STATE: LoadingViewState<SearchViewState>
    lateinit var LOADING_STATE: LoadingViewState<SearchViewState>

    val NETWORK_ERROR = LoadingViewStateError("Network Error", R.drawable.ic_error_outline_black_24dp, true)
    lateinit var NETWORK_ERROR_STATE: LoadingViewState<SearchViewState>

    lateinit var SUCCESS_STATE: LoadingViewState<SearchViewState>

    @Before fun setUp() {
        viewStateFactory = SearchViewStateFactory(controllerRule.activity)
        EMPTY_STATE = viewStateFactory.viewState(AsyncResult.success(SearchResult.EmptyQuery))
        LOADING_STATE = viewStateFactory.viewState(AsyncResult.loading(null))
        NETWORK_ERROR_STATE = viewStateFactory.viewState(AsyncResult.failure(NETWORK_ERROR))
        SUCCESS_STATE = viewStateFactory.viewState(AsyncResult.success(SearchResult.Some(listOf(MOVIE1, MOVIE2))))
    }

    @Test
    fun showsEmptySearchState() {
        controllerRule.setViewState(EMPTY_STATE)

        controllerRule.takeScreenshot("showsEmptySearchState")
        onView(withId(R.id.error_text))
                .check(matches(withText(EMPTY_STATE.errorText)))
    }

    @Test
    fun showsLoadingState() {
        onView(isAssignableFrom(ProgressBar::class.java)).perform(ProgressBarViewActions.replaceProgressBarDrawable())

        controllerRule.setViewState(LOADING_STATE)

        controllerRule.takeScreenshot("showsLoadingState")
        onView(withId(R.id.loading_indicator))
                .check(matches(isDisplayed()))
    }

    @Test
    fun showsErrorState() {
        controllerRule.setViewState(NETWORK_ERROR_STATE)

        controllerRule.takeScreenshot("showsErrorState")
        onView(withId(R.id.error_text))
                .check(matches(withText(NETWORK_ERROR.message)))
        onView(withId(R.id.error_try_again))
                .check(matches(isDisplayed()))
    }

    @Test
    fun showsResults() {
        controllerRule.setViewState(SUCCESS_STATE)

        controllerRule.takeScreenshot()

        onView(withRecyclerView(R.id.movies_list).atPosition(0))
                .check(matches(hasDescendant(withText(MOVIE1.title))))
        onView(withRecyclerView(R.id.movies_list).atPosition(1))
                .check(matches(hasDescendant(withText(MOVIE2.title))))
    }
}
