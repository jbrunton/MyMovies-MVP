package com.jbrunton.mymovies.ui.search

import android.view.View
import com.jbrunton.mymovies.R
import com.jbrunton.mymovies.ui.shared.BaseLoadingLayoutManager
import kotlinx.android.synthetic.main.fragment_search.*

class SearchLayoutManager: BaseLoadingLayoutManager<SearchViewState>()
{
    override val layout: Int = R.layout.fragment_search
    override val contentView: View get() = movies_list
    private lateinit var searchResultsAdapter: SearchResultsAdapter

    override fun bind(view: View) {
        super.bind(view)

        val context = containerView.context
        movies_list.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)

        searchResultsAdapter = SearchResultsAdapter(context, R.layout.item_movie_card_list)
        movies_list.adapter = searchResultsAdapter
    }

    override fun updateContentView(viewState: SearchViewState) {
        searchResultsAdapter.setDataSource(viewState.results)
    }
}