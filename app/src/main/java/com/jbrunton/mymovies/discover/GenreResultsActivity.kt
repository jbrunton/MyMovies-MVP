package com.jbrunton.mymovies.discover

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.View
import com.jbrunton.mymovies.R
import com.jbrunton.mymovies.helpers.observe
import com.jbrunton.mymovies.helpers.toVisibility
import com.jbrunton.mymovies.search.SearchResultsAdapter
import com.jbrunton.mymovies.search.SearchViewState
import com.jbrunton.mymovies.shared.BaseActivity
import kotlinx.android.synthetic.main.activity_genre_results.*

class GenreResultsActivity : BaseActivity<GenreResultsViewModel>() {
    private lateinit var moviesAdapter: SearchResultsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_genre_results)

        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        moviesAdapter = SearchResultsAdapter(this, R.layout.item_movie_card_list)
        movies_list.adapter = moviesAdapter
        movies_list.layoutManager = LinearLayoutManager(this)

        viewModel.viewState.observe(this, this::updateView)
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

    override fun provideViewModel(): GenreResultsViewModel {
        val factory = GenreResultsViewModel.Factory(
                intent.extras!!.getString("GENRE_ID"), dependencies().moviesRepository)
        return getViewModel(GenreResultsViewModel::class.java, factory)
    }

    private fun updateView(viewState: SearchViewState) {
        updateLoadingView(viewState.loadingViewState)
        movies_list.visibility = toVisibility(!viewState.loadingViewState.showError())
        moviesAdapter!!.setDataSource(viewState.movies)
    }
}