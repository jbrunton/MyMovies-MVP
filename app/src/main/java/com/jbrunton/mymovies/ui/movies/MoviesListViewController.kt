package com.jbrunton.mymovies.ui.movies

import android.view.View
import com.jbrunton.entities.models.Movie
import com.jbrunton.libs.ui.BaseLoadingViewController
import com.jbrunton.mymovies.R
import com.jbrunton.mymovies.ui.search.SearchViewState
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.activity_genre_results.*

class MoviesListViewController(override val layout: Int, val onMovieSelected: (Movie) -> Unit) : BaseLoadingViewController<SearchViewState>(), LayoutContainer {
    override val contentView: View get() = movies_list
    private lateinit var moviesAdapter: MoviesListAdapter

    override fun bind(containerView: View) {
        super.bind(containerView)
        moviesAdapter = MoviesListAdapter(context, R.layout.item_movie_card_list, onMovieSelected)
        movies_list.adapter = moviesAdapter
        movies_list.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
    }

    override fun updateContentView(viewState: SearchViewState) {
        moviesAdapter.setDataSource(viewState.results)
    }
}