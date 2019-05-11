package com.jbrunton.mymovies.ui.moviedetails

import android.view.View
import androidx.appcompat.widget.Toolbar
import com.jbrunton.inject.injectViewModel
import com.jbrunton.inject.parametersOf
import com.jbrunton.mymovies.R
import com.jbrunton.mymovies.libs.ui.controllers.rootView
import com.jbrunton.mymovies.libs.ui.livedata.observe
import com.jbrunton.mymovies.libs.ui.views.BaseActivity

class MovieDetailsActivity : BaseActivity<MovieDetailsViewModel>() {
    val toolbar: Toolbar get() = findViewById(R.id.toolbar)
    val favorite: View get() = findViewById(R.id.favorite)
    val unfavorite: View get() = findViewById(R.id.unfavorite)

    override val viewModel: MovieDetailsViewModel by injectViewModel { parametersOf(movieId()) }
    private val viewController = MovieDetailsViewController()

    override fun onCreateLayout() {
        setContentView(R.layout.activity_movie_details)

        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        title = ""

        viewController.onViewCreated(rootView)
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
