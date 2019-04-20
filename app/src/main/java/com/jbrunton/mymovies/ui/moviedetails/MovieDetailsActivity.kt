package com.jbrunton.mymovies.ui.moviedetails

import com.jbrunton.inject.injectViewModel
import com.jbrunton.inject.parametersOf
import com.jbrunton.libs.ui.BaseActivity
import com.jbrunton.libs.ui.observe
import com.jbrunton.mymovies.R
import kotlinx.android.synthetic.main.activity_movie_details.*
import kotlinx.android.synthetic.main.item_movie_card_list.*

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
        viewController.error_try_again.setOnClickListener { viewModel.onRetryClicked() }
        favorite.setOnClickListener { viewModel.onFavoriteClicked() }
        unfavorite.setOnClickListener { viewModel.onUnfavoriteClicked() }
    }

    override fun onObserveData() {
        super.onObserveData()
        viewModel.viewState.observe(this, viewController::updateView)
    }

    private fun movieId(): String = intent.extras["MOVIE_ID"] as String
}
