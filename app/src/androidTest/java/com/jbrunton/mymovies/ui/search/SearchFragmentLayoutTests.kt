package com.jbrunton.mymovies.ui.search

import android.widget.ProgressBar
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.runner.AndroidJUnit4
import com.jbrunton.async.*
import com.jbrunton.entities.models.Movie
import com.jbrunton.fixtures.MovieFactory
import com.jbrunton.mymovies.R
import com.jbrunton.mymovies.fixtures.BaseFragmentTest
import com.jbrunton.mymovies.fixtures.FragmentTestRule
import com.jbrunton.mymovies.fixtures.ProgressBarViewActions
import com.jbrunton.mymovies.fixtures.RecyclerViewUtils.withRecyclerView
import com.jbrunton.mymovies.ui.shared.LoadingViewState
import com.jbrunton.mymovies.ui.shared.LoadingViewStateError
import com.jbrunton.mymovies.ui.shared.toLoadingViewState
import org.junit.Test
import org.junit.runner.RunWith
import java.util.Arrays.asList


@RunWith(AndroidJUnit4::class)
class SearchFragmentLayoutTests : BaseFragmentTest<SearchFragment>() {
    val MOVIE_FACTORY = MovieFactory()
    val MOVIE1 = MOVIE_FACTORY.create()
    val MOVIE2 = MOVIE_FACTORY.create()

    val EMPTY_STATE = SearchViewStateFactory.emptyState
    val LOADING_STATE = AsyncResult.Loading<SearchViewState>()

    private val NETWORK_ERROR = LoadingViewStateError("Network Error", R.drawable.ic_error_outline_black_24dp, true)

    @Test
    fun showsEmptySearchState() {
        setViewState(EMPTY_STATE.toLoadingViewState(emptyList()))

        takeScreenshot("showsEmptySearchState")
        onView(withId(R.id.error_text))
                // TODO: externalize strings
                .check(matches(withText(EMPTY_STATE.error.message)))
    }

    @Test
    fun showsLoadingState() {
        onView(isAssignableFrom(ProgressBar::class.java)).perform(ProgressBarViewActions.replaceProgressBarDrawable())

        setViewState(LOADING_STATE.toLoadingViewState(emptyList()))

        takeScreenshot("showsLoadingState")
        onView(withId(R.id.loading_indicator))
                .check(matches(isDisplayed()))
    }

    @Test
    fun showsErrorState() {
        setViewState(AsyncResult.Failure<SearchViewState>(NETWORK_ERROR).toLoadingViewState(emptyList()))

        takeScreenshot("showsErrorState")
        onView(withId(R.id.error_text))
                .check(matches(withText(NETWORK_ERROR.message)))
        onView(withId(R.id.error_try_again))
                .check(matches(isDisplayed()))
    }

    @Test
    fun showsResults() {
        setViewState(toViewState(asList(MOVIE1, MOVIE2)).toLoadingViewState(emptyList()))

        takeScreenshot()

        onView(withRecyclerView(R.id.movies_list).atPosition(0))
                .check(matches(hasDescendant(withText(MOVIE1.title))))
        onView(withRecyclerView(R.id.movies_list).atPosition(1))
                .check(matches(hasDescendant(withText(MOVIE2.title))))
    }

    override fun createFragmentTestRule(): FragmentTestRule<*, SearchFragment> {
        return FragmentTestRule.create(SearchFragment::class.java)
    }

    private fun setViewState(viewState: LoadingViewState<SearchViewState>) {
        fragmentRule.runOnUiThread { fragment.updateView(viewState) }
    }

    private fun toViewState(movies: List<Movie>): AsyncResult<SearchViewState> {
        return AsyncResult.Success(movies).map(SearchViewStateFactory.Companion::toViewState)
    }
}
