package com.jbrunton.mymovies.features.discover.views

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jbrunton.mymovies.entities.models.Movie
import com.jbrunton.mymovies.features.discover.DiscoverIntent
import com.jbrunton.mymovies.features.discover.DiscoverListener
import com.jbrunton.mymovies.shared.ui.MovieSearchResultViewState
import com.jbrunton.mymovies.shared.ui.MoviesListAdapter
import com.jbrunton.mymovies.shared.ui.R

class MoviesGridView(context: Context, attrs: AttributeSet): RecyclerView(context, attrs) {
    private lateinit var listener: DiscoverListener

    init {
        adapter = MoviesListAdapter(context, R.layout.item_movie_card_grid, this::onMovieSelected)
        layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    }

    fun updateView(viewState: List<MovieSearchResultViewState>) {
        (adapter as MoviesListAdapter).setDataSource(viewState)
    }

    fun setListener(listener: DiscoverListener) {
        this.listener = listener
    }

    private fun onMovieSelected(movie: Movie) {
        listener.perform(DiscoverIntent.SelectMovie(movie))
    }
}