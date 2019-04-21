package com.jbrunton.mymovies.shared.ui

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.jbrunton.entities.models.Movie
import com.jbrunton.libs.ui.BaseLoadingViewController

class MoviesListViewController(
        override val layout: Int,
        val contentViewId: Int,
        val onMovieSelected: (Movie) -> Unit
) : BaseLoadingViewController<SearchViewState>() {
    override val contentView: RecyclerView get() = containerView.findViewById(contentViewId)
    private lateinit var moviesAdapter: MoviesListAdapter

    override fun bind(containerView: View) {
        super.bind(containerView)
        moviesAdapter = MoviesListAdapter(context, R.layout.item_movie_card_list, onMovieSelected)
        contentView.adapter = moviesAdapter
        contentView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
    }

    override fun updateContentView(viewState: SearchViewState) {
        moviesAdapter.setDataSource(viewState.results)
    }
}
