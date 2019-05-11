package com.jbrunton.mymovies.features.account.favorites

import androidx.recyclerview.widget.RecyclerView
import com.jbrunton.mymovies.entities.models.Movie
import com.jbrunton.mymovies.features.account.R
import com.jbrunton.mymovies.shared.ui.MoviesListViewController

class FavoritesViewController(val viewModel: FavoritesViewModel) : MoviesListViewController() {
    override val contentView: RecyclerView get() = view.findViewById(R.id.movies_list)
    override fun onMovieSelected(movie: Movie) = viewModel.onMovieSelected(movie)
}