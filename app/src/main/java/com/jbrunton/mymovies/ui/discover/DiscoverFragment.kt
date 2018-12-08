package com.jbrunton.mymovies.ui.discover

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jbrunton.inject.injectViewModel
import com.jbrunton.mymovies.R
import com.jbrunton.mymovies.helpers.observe
import com.jbrunton.mymovies.ui.search.SearchResultsAdapter
import com.jbrunton.mymovies.ui.search.SearchViewState
import com.jbrunton.mymovies.ui.shared.BaseFragment
import com.jbrunton.mymovies.ui.shared.LoadingLayoutManager
import com.jbrunton.mymovies.ui.shared.LoadingViewState
import kotlinx.android.synthetic.main.fragment_discover.*
import kotlinx.android.synthetic.main.layout_loading_state.*

class DiscoverFragment : BaseFragment<DiscoverViewModel>() {
    private lateinit var loadingLayoutManager: LoadingLayoutManager
    private lateinit var nowPlayingAdapter: SearchResultsAdapter

    override val viewModel: DiscoverViewModel by injectViewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_discover, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadingLayoutManager = LoadingLayoutManager.buildFor(this, discover_content)
        nowPlayingAdapter = SearchResultsAdapter(activity!!, R.layout.item_movie_card_grid)
        now_playing.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(activity, androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL, false)
        now_playing.adapter = nowPlayingAdapter
    }

    override fun onBindListeners() {
        genres_link.setOnClickListener {
            val intent = Intent(activity, GenresActivity::class.java)
            startActivity(intent)
        }

        error_try_again.setOnClickListener { viewModel.retry() }
    }

    override fun onObserveData() {
        viewModel.viewState.observe(viewLifecycleOwner, this::updateView)
    }

    private fun updateView(viewState: LoadingViewState<SearchViewState>) {
        loadingLayoutManager.updateLayout(viewState, nowPlayingAdapter::setDataSource)
    }
}
