package com.jbrunton.mymovies.shared

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.jbrunton.entities.models.LoadingState
import com.jbrunton.entities.models.Movie
import com.jbrunton.fixtures.MovieFactory
import com.jbrunton.mymovies.R
import com.jbrunton.mymovies.fixtures.TestActivity
import com.jbrunton.mymovies.moviedetails.MovieDetailsViewState
import com.jbrunton.mymovies.movies.MovieViewState
import org.junit.Before
import org.robolectric.Robolectric




@RunWith(RobolectricTestRunner::class)
class LoadingLayoutManagerTest {
    val movies = MovieFactory()
    lateinit var view: View
    lateinit var layoutManager: LoadingLayoutManager
    lateinit var contentView: View

    @Before
    fun setUp() {
        val activity = Robolectric.setupActivity(TestActivity::class.java)
        view = LayoutInflater.from(activity).inflate(R.layout.activity_movie_details, null)
        contentView = view.findViewById(R.id.content)
        layoutManager = LoadingLayoutManager(view, contentView)
    }

    @Test
    fun showsContentOnSuccessState() {
        val state = LoadingState.Success(MovieViewState(movies.create()))
        val overviewView = view.findViewById<TextView>(R.id.overview)
        layoutManager.updateLayout(state) {
            overviewView.text = it.overview
        }
        assertThat(contentView.visibility).isEqualTo(View.VISIBLE)
        assertThat(overviewView.text.toString()).isEqualTo(state.value.overview)
    }
}