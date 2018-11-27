package com.jbrunton.mymovies.discover

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.Toolbar
import com.jakewharton.rxbinding2.view.clicks
import com.jbrunton.entities.models.AsyncResult
import com.jbrunton.mymovies.R
import com.jbrunton.mymovies.helpers.observe
import com.jbrunton.mymovies.search.SearchResultsAdapter
import com.jbrunton.mymovies.search.SearchViewState
import com.jbrunton.mymovies.shared.BaseActivity
import com.jbrunton.mymovies.shared.LoadingLayoutManager
import com.jbrunton.mymovies.shared.LoadingViewState
import com.trello.rxlifecycle2.android.lifecycle.kotlin.bindToLifecycle
import kotlinx.android.synthetic.main.activity_genre_results.*
import kotlinx.android.synthetic.main.layout_loading_state.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class GenreResultsActivity : BaseActivity<GenreResultsViewModel>() {
    private lateinit var moviesAdapter: SearchResultsAdapter
    private lateinit var loadingLayoutManager: LoadingLayoutManager

    lateinit var viewModel: GenreResultsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_genre_results)

        viewModel = resolveViewModel { parametersOf(genreId()) }

        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        loadingLayoutManager = LoadingLayoutManager.buildFor(this, movies_list)

        moviesAdapter = SearchResultsAdapter(this, R.layout.item_movie_card_list)
        movies_list.adapter = moviesAdapter
        movies_list.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)

        viewModel.viewState.observe(this, this::updateView)
        viewModel.start()

        error_try_again.clicks()
                .bindToLifecycle(this)
                .subscribe { viewModel.retry() }
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

    private fun genreId(): String = intent.extras["GENRE_ID"] as String

    private fun updateView(viewState: LoadingViewState<SearchViewState>) {
        loadingLayoutManager.updateLayout(viewState, moviesAdapter::setDataSource)
    }
}
