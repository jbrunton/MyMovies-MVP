package com.jbrunton.mymovies.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.jbrunton.inject.injectViewModel
import com.jbrunton.mymovies.R
import com.jbrunton.mymovies.helpers.observe
import com.jbrunton.mymovies.ui.shared.BaseFragment
import com.jbrunton.mymovies.ui.shared.LoadingLayoutManager
import com.jbrunton.mymovies.ui.shared.LoadingViewState
import com.jbrunton.mymovies.ui.shared.onTextChanged
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.layout_loading_state.*

class SearchFragment : BaseFragment<SearchViewModel>() {
    override val viewModel: SearchViewModel by injectViewModel()
    lateinit var layoutManager: LayoutManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onCreateLayout() {
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        layoutManager = LayoutManager(view)
    }

    override fun onBindListeners() {
        search_query.onTextChanged(coroutineContext) { performSearch() }
        error_try_again.setOnClickListener { performSearch() }
    }

    override fun onObserveData() {
        viewModel.viewState.observe(viewLifecycleOwner, this::updateView)
    }

    private fun performSearch() {
        viewModel.performSearch(search_query.text.toString())
    }

    fun updateView(viewState: LoadingViewState<SearchViewState>) {
        layoutManager.updateView(viewState)
    }

    class LayoutManager(override val containerView: View?) : LayoutContainer {
        private val loadingLayoutManager: LoadingLayoutManager
        private val searchResultsAdapter: SearchResultsAdapter

        init {
            val context = containerView!!.context
            loadingLayoutManager = LoadingLayoutManager.buildFor(this, movies_list)
            movies_list.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)

            searchResultsAdapter = SearchResultsAdapter(context, R.layout.item_movie_card_list)
            movies_list.adapter = searchResultsAdapter
        }

        fun updateView(viewState: LoadingViewState<SearchViewState>) {
            loadingLayoutManager.updateLayout(viewState) {
                searchResultsAdapter.setDataSource(it.results)
            }
        }
    }
}
