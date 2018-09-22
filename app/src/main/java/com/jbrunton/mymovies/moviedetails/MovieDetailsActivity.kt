package com.jbrunton.mymovies.moviedetails

import androidx.lifecycle.Observer
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import android.view.MenuItem
import android.view.View
import com.jbrunton.mymovies.R
import com.jbrunton.mymovies.helpers.PicassoHelper
import com.jbrunton.mymovies.shared.BaseActivity
import kotlinx.android.synthetic.main.activity_movie_details.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MovieDetailsActivity : BaseActivity<MovieDetailsViewModel>() {
    val viewModel: MovieDetailsViewModel by viewModel { movieId() }
    private val picassoHelper = PicassoHelper()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_details)
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        title = ""
        error_try_again.setOnClickListener { viewModel.retry() }

        viewModel.viewState().observe(this, Observer<MovieDetailsViewState> { this.updateView(it!!) })
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

    private fun updateView(viewState: MovieDetailsViewState) {
        updateLoadingView(viewState.loadingViewState)

        title = viewState.movie.title
        overview.text = viewState.movie.overview

        picassoHelper.loadImage(this, backdrop, viewState.movie.backdropUrl)

        updateLoadingView(viewState.loadingViewState)
    }
}
