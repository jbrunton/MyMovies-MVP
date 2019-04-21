package com.jbrunton.mymovies.ui.account.favorites

import com.jbrunton.inject.injectViewModel
import com.jbrunton.libs.ui.BaseActivity
import com.jbrunton.libs.ui.observe
import com.jbrunton.mymovies.R
import com.jbrunton.mymovies.ui.movies.MoviesListViewController
import kotlinx.android.synthetic.main.activity_genre_results.*

class FavoritesActivity : BaseActivity<FavoritesViewModel>() {
    private val layoutController = MoviesListViewController(R.layout.activity_favorites)

    override val viewModel: FavoritesViewModel by injectViewModel()

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
