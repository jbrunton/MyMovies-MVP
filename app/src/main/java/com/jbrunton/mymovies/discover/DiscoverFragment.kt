package com.jbrunton.mymovies.discover

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.jakewharton.rxbinding2.view.clicks
import com.jbrunton.mymovies.R
import com.jbrunton.mymovies.helpers.observe
import com.jbrunton.mymovies.helpers.toVisibility
import com.jbrunton.mymovies.search.SearchResultsAdapter
import com.jbrunton.mymovies.search.SearchViewState
import com.jbrunton.mymovies.shared.BaseFragment
import com.jbrunton.mymovies.shared.LoadingViewState
import com.jbrunton.mymovies.shared.Success
import com.trello.rxlifecycle2.android.lifecycle.kotlin.bindToLifecycle
import kotlinx.android.synthetic.main.fragment_discover.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class DiscoverFragment : BaseFragment<DiscoverViewModel>() {
    private lateinit var nowPlayingAdapter: SearchResultsAdapter

    private val viewModel: DiscoverViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_discover, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        nowPlayingAdapter = SearchResultsAdapter(activity!!, R.layout.item_movie_card_grid)
        now_playing.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(activity, androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL, false)
        now_playing.adapter = nowPlayingAdapter

        genres_link.clicks()
                .bindToLifecycle(this)
                .subscribe {
                    val intent = Intent(activity, GenresActivity::class.java)
                    startActivity(intent)
                }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.viewState.observe(this, this::updateView)
        viewModel.start()
    }

    private fun updateView(viewState: LoadingViewState<SearchViewState>) {
        discover_content.visibility = toVisibility(viewState is Success)
        if (viewState is Success) {
            nowPlayingAdapter.setDataSource(viewState.value)
        }
        updateLoadingView(viewState)
    }
}
