package com.jbrunton.mymovies.ui.search

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.runner.AndroidJUnit4
import com.jbrunton.mymovies.R
import com.jbrunton.mymovies.entities.repositories.MoviesRepository
import com.jbrunton.mymovies.features.search.SearchFragment
import com.jbrunton.mymovies.fixtures.MovieFactory
import com.jbrunton.mymovies.fixtures.RecyclerViewUtils.withRecyclerView
import com.jbrunton.mymovies.fixtures.repositories.stubSearch
import com.jbrunton.mymovies.fixtures.rules.FragmentTestRule
import com.jbrunton.mymovies.fixtures.rules.takeScreenshot
import com.jbrunton.mymovies.libs.ui.DebounceTextWatcher
import kotlinx.coroutines.test.TestCoroutineContext
import kotlinx.coroutines.test.TestCoroutineScope
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.test.KoinTest
import org.koin.test.inject
import java.util.concurrent.TimeUnit
import kotlin.coroutines.CoroutineContext


@RunWith(AndroidJUnit4::class)
class SearchFragmentTest : KoinTest {
    @get:Rule val fragmentTestRule = FragmentTestRule.create(SearchFragment::class.java)

    val MOVIE_FACTORY = MovieFactory()
    val MOVIE1 = MOVIE_FACTORY.create()
    val MOVIE2 = MOVIE_FACTORY.create()

    val moviesRepository: MoviesRepository by inject()
    val coroutineContext: CoroutineContext by inject()
    val testCoroutineContext = coroutineContext as TestCoroutineContext

    @Test
    fun defaultsToSearchFragment() {
        fragmentTestRule.takeScreenshot()
        onView(withId(R.id.search_query)).check(matches(isDisplayed()))
    }

    @Test
    fun performsASearch() {
        moviesRepository.stubSearch("Star Wars", listOf(MOVIE1, MOVIE2))

        onView(withId(R.id.search_query)).perform(ViewActions.replaceText("Star Wars"))
        testCoroutineContext.advanceTimeBy(DebounceTextWatcher.Delay, TimeUnit.MILLISECONDS)

        fragmentTestRule.takeScreenshot()
        onView(withRecyclerView(R.id.movies_list).atPosition(0))
                .check(matches(hasDescendant(withText(MOVIE1.title))))
        onView(withRecyclerView(R.id.movies_list).atPosition(1))
                .check(matches(hasDescendant(withText(MOVIE2.title))))
    }
}
