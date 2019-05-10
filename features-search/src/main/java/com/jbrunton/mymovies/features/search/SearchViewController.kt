package com.jbrunton.mymovies.features.search

import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import com.jbrunton.mymovies.entities.models.Movie
import com.jbrunton.mymovies.libs.kotterknife.bindView
import com.jbrunton.mymovies.libs.ui.onTextChanged
import com.jbrunton.mymovies.shared.ui.MoviesListViewController
import kotlin.coroutines.CoroutineContext

class SearchViewController(val viewModel: SearchViewModel) : MoviesListViewController() {
    override val contentView: RecyclerView get() = view.findViewById(R.id.movies_list)
    private val search_query: EditText by bindView(R.id.search_query)

    override fun onMovieSelected(movie: Movie) = viewModel.onMovieSelected(movie)

    fun bindListeners(coroutineContext: CoroutineContext) {
        search_query.onTextChanged(coroutineContext) { performSearch() }
        error_try_again.setOnClickListener { performSearch() }
    }

    private fun performSearch() {
        viewModel.onSearchQueryChanged(search_query.text.toString())
    }
}