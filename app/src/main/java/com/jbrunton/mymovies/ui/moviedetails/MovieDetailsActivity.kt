package com.jbrunton.mymovies.ui.moviedetails

import com.jbrunton.inject.injectViewModel
import com.jbrunton.inject.parametersOf
import com.jbrunton.mymovies.R
import com.jbrunton.mymovies.helpers.observe
import com.jbrunton.mymovies.ui.shared.BaseActivity
import kotlinx.android.synthetic.main.activity_movie_details.*
import kotlinx.android.synthetic.main.item_movie_card_list.*
import kotlinx.android.synthetic.main.layout_loading_state.*

class MovieDetailsActivity : BaseActivity<MovieDetailsViewModel>() {
    override val viewModel: MovieDetailsViewModel by injectViewModel { parametersOf(movieId()) }
    private val viewController = MovieDetailsViewController()

    override fun onCreateLayout() {
        setContentView(R.layout.activity_movie_details)

        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        title = ""

        viewController.bind(this)
    }

    override fun onBindListeners() {
        error_try_again.setOnClickListener { viewModel.retry() }
        favorite.setOnClickListener { viewModel.favorite() }
        unfavorite.setOnClickListener { viewModel.unfavorite() }
    }

    override fun onObserveData() {
        super.onObserveData()
        viewModel.viewState.observe(this, viewController::updateView)
    }

    private fun movieId(): String = intent.extras["MOVIE_ID"] as String
}
