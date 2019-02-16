package com.jbrunton.mymovies.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.jbrunton.inject.injectViewModel
import com.jbrunton.mymovies.helpers.observe
import com.jbrunton.mymovies.ui.shared.BaseFragment
import com.jbrunton.mymovies.ui.shared.onTextChanged
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.layout_loading_state.*

class SearchFragment : BaseFragment<SearchViewModel>() {
    override val viewModel: SearchViewModel by injectViewModel()
    val layoutController = SearchViewController()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(layoutController.layout, container, false)
    }

    override fun onCreateLayout() {
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        layoutController.bind(this)
    }

    override fun onBindListeners() {
        search_query.onTextChanged(coroutineContext) { performSearch() }
        error_try_again.setOnClickListener { performSearch() }
    }

    override fun onObserveData() {
        viewModel.viewState.observe(viewLifecycleOwner, layoutController::updateView)
    }

    private fun performSearch() {
        viewModel.performSearch(search_query.text.toString())
    }
}
