package com.jbrunton.mymovies.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.runner.AndroidJUnit4
import com.jbrunton.async.AsyncResult
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


@RunWith(AndroidJUnit4::class)
class SearchFragmentLayoutTest : BaseFragmentTest<SearchFragmentLayoutTest.TestFragment>() {
    val MOVIE_FACTORY = MovieFactory()
    val MOVIE1 = MOVIE_FACTORY.create()
    val MOVIE2 = MOVIE_FACTORY.create()

    val EMPTY_STATE = SearchViewStateFactory.EmptyState
    val LOADING_STATE = AsyncResult.Loading<SearchViewState>()
            .toLoadingViewState(SearchViewState.Empty)

    val NETWORK_ERROR = LoadingViewStateError("Network Error", R.drawable.ic_error_outline_black_24dp, true)
    val NETWORK_ERROR_STATE = AsyncResult.Failure<SearchViewState>(NETWORK_ERROR)
            .toLoadingViewState(SearchViewState.Empty)

    val SUCCESS_STATE = AsyncResult.Success(SearchViewState.from(listOf(MOVIE1, MOVIE2)))
            .toLoadingViewState(SearchViewState.Empty)

    @Test
    fun showsEmptySearchState() {
        setViewState(EMPTY_STATE)

        takeScreenshot("showsEmptySearchState")
        onView(withId(R.id.error_text))
                .check(matches(withText(EMPTY_STATE.errorText)))
    }

    @Test
    fun showsLoadingState() {
        onView(isAssignableFrom(ProgressBar::class.java)).perform(ProgressBarViewActions.replaceProgressBarDrawable())

        setViewState(LOADING_STATE)

        takeScreenshot("showsLoadingState")
        onView(withId(R.id.loading_indicator))
                .check(matches(isDisplayed()))
    }

    @Test
    fun showsErrorState() {
        setViewState(NETWORK_ERROR_STATE)

        takeScreenshot("showsErrorState")
        onView(withId(R.id.error_text))
                .check(matches(withText(NETWORK_ERROR.message)))
        onView(withId(R.id.error_try_again))
                .check(matches(isDisplayed()))
    }

    @Test
    fun showsResults() {
        setViewState(SUCCESS_STATE)

        takeScreenshot()

        onView(withRecyclerView(R.id.movies_list).atPosition(0))
                .check(matches(hasDescendant(withText(MOVIE1.title))))
        onView(withRecyclerView(R.id.movies_list).atPosition(1))
                .check(matches(hasDescendant(withText(MOVIE2.title))))
    }

    override fun createFragmentTestRule(): FragmentTestRule<*, TestFragment> {
        return FragmentTestRule.create(TestFragment::class.java)
    }

    private fun setViewState(viewState: LoadingViewState<SearchViewState>) {
        fragmentRule.runOnUiThread { fragment.updateView(viewState) }
    }

    class TestFragment: Fragment() {
        lateinit var layoutManager: SearchFragment.LayoutManager

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
            return inflater.inflate(R.layout.fragment_search, container, false)
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
            layoutManager = SearchFragment.LayoutManager(view)
        }

        fun updateView(viewState: LoadingViewState<SearchViewState>) {
            layoutManager.updateView(viewState)
        }
    }
}
