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
import com.jbrunton.mymovies.movies.MovieViewState
import kotlinx.android.synthetic.main.content_movie_details.*
import org.junit.Before
import org.junit.Rule




@RunWith(AndroidJUnit4::class)
class LoadingLayoutManagerTest {
    val movies = MovieFactory()
    lateinit var view: View
    lateinit var layoutManager: LoadingLayoutManager

    @get:Rule
    val rule = ActivityTestRule(MyTestActivity::class.java)

    @Before
    fun setUp() {
        view = rule.activity.findViewById(android.R.id.content)
        layoutManager = rule.activity.layoutManager
    }

    @Test
    fun showsContentOnSuccessState() {
        val state = LoadingState.Success(MovieViewState(movies.create()))
        val overviewView = view.findViewById<TextView>(R.id.overview)

        layoutManager.updateLayout(state) {
            overviewView.text = it.overview
        }

        onView(withId(R.id.content)).check(matches(isDisplayed()))
        onView(withId(R.id.overview)).check(matches(withText(state.value.overview)))
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