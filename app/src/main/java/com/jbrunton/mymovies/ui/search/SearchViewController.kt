package com.jbrunton.mymovies.ui.search

import android.view.View
import com.jbrunton.libs.ui.BaseLoadingViewController
import com.jbrunton.mymovies.R
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.fragment_search.*

class SearchViewController: BaseLoadingViewController<SearchViewState>(), LayoutContainer
{
    override val layout: Int = R.layout.fragment_search
    override val contentView: View get() = movies_list
    private lateinit var searchResultsAdapter: SearchResultsAdapter

    override fun bind(containerView: View) {
        super.bind(containerView)

        movies_list.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)

        searchResultsAdapter = SearchResultsAdapter(context, R.layout.item_movie_card_list)
        movies_list.adapter = searchResultsAdapter
    }

    override fun updateContentView(viewState: SearchViewState) {
        searchResultsAdapter.setDataSource(viewState.results)
    }
}