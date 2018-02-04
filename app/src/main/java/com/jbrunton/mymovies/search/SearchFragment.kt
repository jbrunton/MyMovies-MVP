package com.jbrunton.mymovies.search

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.ButterKnife
import com.jbrunton.mymovies.R
import com.jbrunton.mymovies.helpers.afterTextChange
import com.jbrunton.mymovies.helpers.toVisibility
import com.jbrunton.mymovies.shared.BaseFragment
import com.jbrunton.mymovies.shared.LoadingStateContext
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.layout_loading_state.*

class SearchFragment : BaseFragment<SearchViewModel>() {
    private lateinit var searchResultsAdapter: SearchResultsAdapter
    private val loadingStateContext = LoadingStateContext()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        ButterKnife.bind(this, view)
        ButterKnife.bind(this.loadingStateContext, view)

        movies_list.layoutManager = LinearLayoutManager(activity)
        searchResultsAdapter = SearchResultsAdapter(activity, R.layout.item_movie_card_list)
        movies_list.adapter = searchResultsAdapter

        search_query.afterTextChange {
            // TODO: use RxBinding and debounce this
            this.performSearch()
        }

        error_try_again.setOnClickListener {
            this.performSearch()
        }

        super.onViewCreated(view, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as AppCompatActivity).setSupportActionBar(my_toolbar)
        viewModel().viewState().observe(this, Observer<SearchViewState> { this.updateView(it!!) })
    }

    override fun provideViewModel(): SearchViewModel {
        val factory = SearchViewModel.Factory(dependencies().moviesRepository)
        return getViewModel(SearchViewModel::class.java, factory)
    }

    private fun performSearch() {
        viewModel().performSearch(search_query.text.toString())
    }

    fun updateView(viewState: SearchViewState) {
        movies_list.visibility = toVisibility(viewState.loadingViewState().showContent())
        searchResultsAdapter.setDataSource(viewState.movies())
        loadingStateContext.updateView(viewState.loadingViewState())
    }
}
