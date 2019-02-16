package com.jbrunton.mymovies.ui.movies

import android.view.View
import com.jbrunton.mymovies.R
import com.jbrunton.mymovies.ui.search.SearchResultsAdapter
import com.jbrunton.mymovies.ui.search.SearchViewState
import com.jbrunton.mymovies.ui.shared.BaseLoadingViewController
import kotlinx.android.synthetic.main.activity_genre_results.*

class MoviesListViewController : BaseLoadingViewController<SearchViewState>() {
    override val layout = R.layout.activity_favorites
    override val contentView: View get() = movies_list
    private lateinit var moviesAdapter: SearchResultsAdapter

    override fun bind(containerView: View) {
        super.bind(containerView)
        moviesAdapter = SearchResultsAdapter(context, R.layout.item_movie_card_list)
        movies_list.adapter = moviesAdapter
        movies_list.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
    }

    override fun updateContentView(viewState: SearchViewState) {
        moviesAdapter.setDataSource(viewState.results)
    }
}