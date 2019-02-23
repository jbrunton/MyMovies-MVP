package com.jbrunton.mymovies.ui.discover.genres

import com.jbrunton.inject.injectViewModel
import com.jbrunton.inject.parametersOf
import com.jbrunton.mymovies.R
import com.jbrunton.mymovies.helpers.observe
import com.jbrunton.mymovies.ui.movies.MoviesListViewController
import com.jbrunton.mymovies.ui.shared.BaseActivity
import kotlinx.android.synthetic.main.activity_genre_results.*
import kotlinx.android.synthetic.main.layout_loading_state.*

class GenreResultsActivity : BaseActivity<GenreResultsViewModel>() {
    private val viewController = MoviesListViewController()

    override val viewModel: GenreResultsViewModel by injectViewModel { parametersOf(genreId(), genreName()) }

    override fun onCreateLayout() {
        setContentView(R.layout.activity_genre_results)

        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setTitle(genreName())

        viewController.bind(this)
    }

    override fun onBindListeners() {
        error_try_again.setOnClickListener { viewModel.retry() }
    }

    override fun onObserveData() {
        viewModel.viewState.observe(this, viewController::updateView)
    }

    private fun genreId(): String = intent.extras["GENRE_ID"] as String
    private fun genreName(): String = intent.extras["GENRE_NAME"] as String
}
