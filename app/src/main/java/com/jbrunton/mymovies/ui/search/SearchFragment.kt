package com.jbrunton.mymovies.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.jbrunton.entities.models.Movie
import com.jbrunton.inject.injectViewModel
import com.jbrunton.libs.ui.BaseFragment
import com.jbrunton.libs.ui.observe
import com.jbrunton.libs.ui.onTextChanged
import com.jbrunton.mymovies.R
import com.jbrunton.mymovies.shared.ui.MoviesListViewController
import kotlinx.android.synthetic.main.fragment_search.*

class SearchFragment : BaseFragment<SearchViewModel>() {
    override val viewModel: SearchViewModel by injectViewModel()

    val layoutController = object : MoviesListViewController(R.layout.fragment_search) {
        override val contentView: RecyclerView get() = containerView.findViewById(R.id.movies_list)
        override fun onMovieSelected(movie: Movie) = viewModel.onMovieSelected(movie)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(layoutController.layout, container, false)
    }

    override fun onCreateLayout() {
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        layoutController.bind(this)
    }

    override fun onBindListeners() {
        search_query.onTextChanged(coroutineContext) { performSearch() }
        layoutController.error_try_again.setOnClickListener { performSearch() }
    }

    override fun onObserveData() {
        viewModel.viewState.observe(viewLifecycleOwner, layoutController::updateView)
    }

    private fun performSearch() {
        viewModel.onSearchQueryChanged(search_query.text.toString())
    }
}
