package com.jbrunton.mymovies.shared

import org.junit.Test
import org.junit.runner.RunWith
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.jbrunton.entities.models.LoadingState
import com.jbrunton.fixtures.MovieFactory
import com.jbrunton.mymovies.R
import com.jbrunton.mymovies.fixtures.BaseActivityTest
import com.jbrunton.mymovies.moviedetails.MovieDetailsActivity
import com.jbrunton.mymovies.moviedetails.MovieDetailsViewState
import com.jbrunton.mymovies.movies.MovieViewState
import kotlinx.android.synthetic.main.content_movie_details.*
import org.hamcrest.CoreMatchers.*
import org.junit.Before
import org.junit.Rule
import java.io.IOException
import java.net.SocketTimeoutException


@RunWith(AndroidJUnit4::class)
class LoadingLayoutManagerTest : BaseActivityTest<MovieDetailsActivity>() {
    override fun createActivityTestRule(): ActivityTestRule<MovieDetailsActivity> {
        return ActivityTestRule(MovieDetailsActivity::class.java)
    }

    val movies = MovieFactory()
    val movieViewState = MovieViewState(movies.create())

    lateinit var view: View
    lateinit var overviewView: TextView
    lateinit var layoutManager: LoadingLayoutManager

    @Before
    fun setUp() {
        view = activity.findViewById(android.R.id.content)
        overviewView = view.findViewById(R.id.overview)
        layoutManager = activity.loadingLayoutManager
    }

    @Test
    fun showsContentOnSuccessState() {
        val state = LoadingState.Success(movieViewState)

        layoutManager.updateLayout(state) {
            overviewView.text = it.overview
        }

        onView(withId(R.id.content)).check(matches(isDisplayed()))
        onView(withId(R.id.overview)).check(matches(withText(movieViewState.overview)))
        onView(withId(R.id.loading_indicator)).check(matches(not(isDisplayed())))
        onView(withId(R.id.error_case)).check(matches(not(isDisplayed())))
    }

    @Test
    fun showsLoadingIndicatorOnLoadingStateIfNoCachedValue() {
        val state = LoadingState.Loading<MovieViewState>()

        layoutManager.updateLayout(state) {}

        onView(withId(R.id.loading_indicator)).check(matches(isDisplayed()))
        onView(withId(R.id.content)).check(matches(not(isDisplayed())))
        onView(withId(R.id.error_case)).check(matches(not(isDisplayed())))
    }

    @Test
    fun showsCachedValueOnLoadingStateIfPresent() {
        val state = LoadingState.Loading(movieViewState)

        layoutManager.updateLayout(state) {
            overviewView.text = it.overview
        }

        onView(withId(R.id.content)).check(matches(isDisplayed()))
        onView(withId(R.id.overview)).check(matches(withText(movieViewState.overview)))
        onView(withId(R.id.loading_indicator)).check(matches(not(isDisplayed())))
        onView(withId(R.id.error_case)).check(matches(not(isDisplayed())))
    }

    @Test
    fun showsErrorDetailsOnLoadingViewStateError() {
        val state = networkFailure<MovieViewState>()

        layoutManager.updateLayout(state) {}

        onView(withId(R.id.error_case)).check(matches(isDisplayed()))
        onView(withId(R.id.error_text)).check(matches(withText(state.error.message)))
        listOf(R.id.content, R.id.loading_indicator).forEach {
            onView(withId(it)).check(matches(not(isDisplayed())))
        }
    }

    @Test
    fun showsCachedValueOnLoadingViewStateErrorIfPresent() {
        val state = networkFailure<MovieViewState>(cachedValue = movieViewState)

        layoutManager.updateLayout(state) {
            overviewView.text = it.overview
        }

        onView(withId(R.id.content)).check(matches(isDisplayed()))
        onView(withId(R.id.overview)).check(matches(withText(movieViewState.overview)))
        listOf(R.id.loading_indicator, R.id.error_case).forEach {
            onView(withId(it)).check(matches(not(isDisplayed())))
        }
    }

    @Test(expected = UnhandledFailureException::class)
    fun throwsExceptionIfFailureIsUnhandled() {
        val state = LoadingState.Failure(IOException(), cachedValue = movieViewState)
        layoutManager.updateLayout(state) {}
    }

    class MyTestActivity : BaseActivity<BaseViewModel>(){
        lateinit var layoutManager: LoadingLayoutManager
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_movie_details)
            layoutManager = LoadingLayoutManager.buildFor(this, content)
        }
    }
}