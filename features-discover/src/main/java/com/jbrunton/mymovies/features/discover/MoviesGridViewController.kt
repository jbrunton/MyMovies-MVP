package com.jbrunton.mymovies.features.discover

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jbrunton.mymovies.entities.models.Movie
import com.jbrunton.mymovies.libs.kotterknife.bindView
import com.jbrunton.mymovies.libs.ui.controllers.ViewController
import com.jbrunton.mymovies.shared.ui.MovieSearchResultViewState
import com.jbrunton.mymovies.shared.ui.MoviesListAdapter
import com.jbrunton.mymovies.shared.ui.R

class MoviesGridViewController(
        val recyclerViewId: Int,
        val onMovieSelected: (Movie) -> Unit
) : ViewController<List<MovieSearchResultViewState>>() {
    private lateinit var moviesAdapter: MoviesListAdapter
    val recyclerView: RecyclerView by bindView(recyclerViewId)

    override fun onViewCreated(view: View) {
        super.onViewCreated(view)

        moviesAdapter = MoviesListAdapter(context, R.layout.item_movie_card_grid, onMovieSelected)
        recyclerView.adapter = moviesAdapter
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    }

    override fun updateView(viewState: List<MovieSearchResultViewState>) {
        moviesAdapter.setDataSource(viewState)
    }
}
