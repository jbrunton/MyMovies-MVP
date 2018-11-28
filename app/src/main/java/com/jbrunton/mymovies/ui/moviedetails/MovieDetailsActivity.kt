package com.jbrunton.mymovies.ui.moviedetails

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.Toolbar
import com.google.android.material.snackbar.Snackbar
import com.jbrunton.mymovies.R
import com.jbrunton.inject.parametersOf
import com.jbrunton.mymovies.helpers.PicassoHelper
import com.jbrunton.mymovies.helpers.observe
import com.jbrunton.mymovies.ui.movies.MovieViewState
import com.jbrunton.mymovies.ui.shared.BaseActivity
import com.jbrunton.mymovies.ui.shared.LoadingLayoutManager
import com.jbrunton.mymovies.ui.shared.LoadingViewState
import com.jbrunton.mymovies.ui.shared.injectViewModel
import kotlinx.android.synthetic.main.activity_movie_details.*
import kotlinx.android.synthetic.main.content_movie_details.*
import kotlinx.android.synthetic.main.layout_loading_state.*

class MovieDetailsActivity : BaseActivity<MovieDetailsViewModel>() {
    val viewModel: MovieDetailsViewModel by injectViewModel() { parametersOf(movieId()) }
    lateinit var loadingLayoutManager: LoadingLayoutManager
    private val picassoHelper = PicassoHelper()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_details)

        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        loadingLayoutManager = LoadingLayoutManager.buildFor(this, content)

        title = ""
        error_try_again.setOnClickListener { viewModel.retry() }

        viewModel.viewState.observe(this, this::updateView)
        viewModel.showRetrySnackbar.observe(this, this::showSnackbar)
        viewModel.start()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun movieId(): String = intent.extras["MOVIE_ID"] as String

    fun updateView(viewState: LoadingViewState<MovieViewState>) {
        loadingLayoutManager.updateLayout(viewState) {
            title = it.title
            overview.text = it.overview

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
