package com.jbrunton.mymovies.ui.account.favorites

import androidx.recyclerview.widget.RecyclerView
import com.jbrunton.entities.models.Movie
import com.jbrunton.inject.injectViewModel
import com.jbrunton.libs.ui.BaseActivity
import com.jbrunton.libs.ui.observe
import com.jbrunton.mymovies.R
import com.jbrunton.mymovies.shared.ui.MoviesListViewController
import kotlinx.android.synthetic.main.activity_genre_results.*

class FavoritesActivity : BaseActivity<FavoritesViewModel>() {
    override val viewModel: FavoritesViewModel by injectViewModel()

    private val layoutController = object : MoviesListViewController(R.layout.activity_favorites) {
        override val contentView: RecyclerView get() = containerView.findViewById(R.id.movies_list)
        override fun onMovieSelected(movie: Movie) = viewModel.onMovieSelected(movie)
    }

    override fun onCreateLayout() {
        setContentView(R.layout.activity_favorites)

        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        title = "Favorites"

        layoutController.bind(this)
    }

    override fun onBindListeners() {
        layoutController.error_try_again.setOnClickListener { viewModel.retry() }
    }

    override fun onObserveData() {
        super.onObserveData()
        viewModel.viewState.observe(this, layoutController::updateView)
    }
}
