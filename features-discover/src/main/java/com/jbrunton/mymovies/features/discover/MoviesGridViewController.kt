package com.jbrunton.mymovies.features.discover

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jbrunton.mymovies.entities.models.Movie
import com.jbrunton.mymovies.libs.ui.controllers.ViewController
import com.jbrunton.mymovies.shared.ui.MovieSearchResultViewState
import com.jbrunton.mymovies.shared.ui.MoviesListAdapter
import com.jbrunton.mymovies.shared.ui.R

abstract class MoviesGridViewController : ViewController<List<MovieSearchResultViewState>>() {
    private lateinit var moviesAdapter: MoviesListAdapter

    override fun onViewCreated(view: View) {
        super.onViewCreated(view)

        moviesAdapter = MoviesListAdapter(context, R.layout.item_movie_card_grid, this::onMovieSelected)
        recyclerView.adapter = moviesAdapter
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    }

    override fun updateView(viewState: List<MovieSearchResultViewState>) {
        moviesAdapter.setDataSource(viewState)
    }

    abstract fun onMovieSelected(movie: Movie)
    abstract val recyclerView: RecyclerView
}
