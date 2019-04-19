package com.jbrunton.mymovies.ui.account.favorites

import com.jbrunton.inject.injectViewModel
import com.jbrunton.mymovies.R
import com.jbrunton.libs.ui.observe
import com.jbrunton.mymovies.ui.movies.MoviesListViewController
import com.jbrunton.libs.ui.BaseActivity
import kotlinx.android.synthetic.main.activity_genre_results.*
import kotlinx.android.synthetic.main.layout_loading_state.*

class FavoritesActivity : BaseActivity<FavoritesViewModel>() {
    private val layoutController = MoviesListViewController()

    override val viewModel: FavoritesViewModel by injectViewModel()

    override fun onCreateLayout() {
        setContentView(R.layout.activity_favorites)

        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        title = "Favorites"

        layoutController.bind(this)
    }

    override fun onBindListeners() {
        error_try_again.setOnClickListener { viewModel.retry() }
    }

    override fun onObserveData() {
        super.onObserveData()
        viewModel.viewState.observe(this, layoutController::updateView)
    }
}
