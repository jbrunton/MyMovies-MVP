package com.jbrunton.mymovies.ui.moviedetails

import android.text.Html
import com.google.android.material.snackbar.Snackbar
import com.jbrunton.inject.injectViewModel
import com.jbrunton.inject.parametersOf
import com.jbrunton.mymovies.R
import com.jbrunton.mymovies.helpers.PicassoHelper
import com.jbrunton.mymovies.helpers.observe
import com.jbrunton.mymovies.ui.movies.MovieViewState
import com.jbrunton.mymovies.ui.shared.BaseActivity
import com.jbrunton.mymovies.ui.shared.LoadingLayoutManager
import com.jbrunton.mymovies.ui.shared.LoadingViewState
import kotlinx.android.synthetic.main.activity_movie_details.*
import kotlinx.android.synthetic.main.content_movie_details.*
import kotlinx.android.synthetic.main.item_movie_card_list.*
import kotlinx.android.synthetic.main.layout_loading_state.*

class MovieDetailsActivity : BaseActivity<MovieDetailsViewModel>() {
    override val viewModel: MovieDetailsViewModel by injectViewModel { parametersOf(movieId()) }
    lateinit var loadingLayoutManager: LoadingLayoutManager
    private val picassoHelper = PicassoHelper()

    override fun onCreateLayout() {
        setContentView(R.layout.activity_movie_details)

        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        title = ""

        loadingLayoutManager = LoadingLayoutManager.buildFor(this, content)
    }

    override fun onBindListeners() {
        error_try_again.setOnClickListener { viewModel.retry() }
        favorite.setOnClickListener { viewModel.favorite() }
        unfavorite.setOnClickListener { viewModel.unfavorite() }
    }

    override fun onObserveData() {
        super.onObserveData()
        viewModel.viewState.observe(this, this::updateView)
        viewModel.showRetrySnackbar.observe(this, this::showSnackbar)
    }

    private fun movieId(): String = intent.extras["MOVIE_ID"] as String

    fun updateView(viewState: LoadingViewState<MovieViewState>) {
        loadingLayoutManager.updateLayout(viewState) {
            //title = it.title

            movie_title.text = it.title
            rating.text = Html.fromHtml(it.rating)
            overview.text = it.overview
            release_date.text = it.yearReleased
            favorite.visibility = it.favoriteVisibility
            unfavorite.visibility = it.unfavoriteVisibility
            picassoHelper.loadSearchResultImage(this, poster, it.posterUrl)
            picassoHelper.loadImage(this, backdrop, it.backdropUrl)
        }
    }

    fun showSnackbar(unit: Unit) {
        Snackbar.make(findViewById(android.R.id.content), "There was a problem reaching the server", Snackbar.LENGTH_INDEFINITE)
                .setAction("Try Again") {
                    viewModel.retry()
                }
                .show()
    }
}
