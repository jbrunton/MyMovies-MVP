package com.jbrunton.mymovies.features.account.favorites

import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView
import com.jbrunton.mymovies.entities.models.Movie
import com.jbrunton.mymovies.features.account.R
import com.jbrunton.inject.injectViewModel
import com.jbrunton.mymovies.libs.ui.views.BaseActivity
import com.jbrunton.mymovies.libs.ui.livedata.observe
import com.jbrunton.mymovies.shared.ui.movies.list.MoviesListViewController

class FavoritesActivity : BaseActivity<FavoritesViewModel>() {
    override val viewModel: FavoritesViewModel by injectViewModel()

    val toolbar: Toolbar get() = findViewById(R.id.toolbar)

    private val layoutController = object : MoviesListViewController(R.layout.activity_favorites) {
        override val contentView: RecyclerView get() = view.findViewById(R.id.movies_list)
        override fun onMovieSelected(movie: Movie) = viewModel.onMovieSelected(movie)
    }

    override fun onCreateLayout() {
        setContentView(R.layout.activity_favorites)

        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        title = "Favorites"

        layoutController.initializeView(this)
    }

    override fun onBindListeners() {
        layoutController.error_try_again.setOnClickListener { viewModel.retry() }
    }

    override fun onObserveData() {
        super.onObserveData()
        viewModel.viewState.observe(this, layoutController::updateView)
    }
}
