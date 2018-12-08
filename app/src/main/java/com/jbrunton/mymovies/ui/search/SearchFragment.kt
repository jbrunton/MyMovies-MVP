package com.jbrunton.mymovies.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.jakewharton.rxbinding2.view.clicks
import com.jakewharton.rxbinding2.widget.textChanges
import com.jbrunton.inject.inject
import com.jbrunton.inject.injectViewModel
import com.jbrunton.mymovies.R
import com.jbrunton.mymovies.helpers.observe
import com.jbrunton.mymovies.ui.shared.BaseFragment
import com.jbrunton.mymovies.ui.shared.LoadingLayoutManager
import com.jbrunton.mymovies.ui.shared.LoadingViewState
import com.trello.rxlifecycle2.android.lifecycle.kotlin.bindToLifecycle
import io.reactivex.Scheduler
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.layout_loading_state.*
import java.util.concurrent.TimeUnit

class SearchFragment : BaseFragment<SearchViewModel>() {
    private lateinit var loadingLayoutManager: LoadingLayoutManager
    private lateinit var searchResultsAdapter: SearchResultsAdapter

    val viewModel: SearchViewModel by injectViewModel()
    val scheduler: Scheduler by inject()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadingLayoutManager = LoadingLayoutManager.buildFor(this, movies_list)
        movies_list.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(activity)
        searchResultsAdapter = SearchResultsAdapter(activity!!, R.layout.item_movie_card_list)
        movies_list.adapter = searchResultsAdapter

        search_query.textChanges()
                .debounce(500, TimeUnit.MILLISECONDS, scheduler)
                .bindToLifecycle(this)
                .subscribe { this.performSearch() }

        error_try_again.clicks()
                .bindToLifecycle(this)
                .subscribe { this.performSearch() }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as AppCompatActivity).setSupportActionBar(my_toolbar)
        viewModel.viewState.observe(viewLifecycleOwner, this::updateView)
        viewModel.start()
    }

    private fun performSearch() {
        viewModel.performSearch(search_query.text.toString())
    }

    fun updateView(viewState: LoadingViewState<SearchViewState>) {
        loadingLayoutManager.updateLayout(viewState, searchResultsAdapter::setDataSource)
    }
}
