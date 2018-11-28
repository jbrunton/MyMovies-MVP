package com.jbrunton.mymovies.ui.search

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.runner.AndroidJUnit4
import com.jbrunton.entities.repositories.MoviesRepository
import com.jbrunton.fixtures.MovieFactory
import com.jbrunton.mymovies.R
import com.jbrunton.inject.inject
import com.jbrunton.mymovies.fixtures.BaseFragmentTest
import com.jbrunton.mymovies.fixtures.FragmentTestRule
import com.jbrunton.mymovies.fixtures.RecyclerViewUtils.withRecyclerView
import com.jbrunton.mymovies.fixtures.stubSearch
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class SearchFragmentTest : BaseFragmentTest<SearchFragment>() {
    val MOVIE_FACTORY = MovieFactory()
    val MOVIE1 = MOVIE_FACTORY.create()
    val MOVIE2 = MOVIE_FACTORY.create()

    val moviesRepository: MoviesRepository by inject()

    @Test
    fun defaultsToSearchFragment() {
        takeScreenshot()
        onView(withId(R.id.search_query)).check(matches(isDisplayed()))
    }

    @Test
    fun performsASearch() {
        moviesRepository.stubSearch("Star Wars", listOf(MOVIE1, MOVIE2))

        onView(withId(R.id.search_query)).perform(ViewActions.replaceText("Star Wars"))

        takeScreenshot()
        onView(withRecyclerView(R.id.movies_list).atPosition(0))
                .check(matches(hasDescendant(withText(MOVIE1.title))))
        onView(withRecyclerView(R.id.movies_list).atPosition(1))
                .check(matches(hasDescendant(withText(MOVIE2.title))))
    }

    override fun createFragmentTestRule(): FragmentTestRule<*, SearchFragment> {
        return FragmentTestRule.create(SearchFragment::class.java)
    }
}
