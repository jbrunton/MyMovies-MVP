package com.jbrunton.mymovies.discover

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.Toolbar
import com.jbrunton.mymovies.R
import com.jbrunton.mymovies.helpers.observe
import com.jbrunton.mymovies.search.SearchResultsAdapter
import com.jbrunton.mymovies.search.SearchViewState
import com.jbrunton.mymovies.shared.BaseActivity
import com.jbrunton.mymovies.shared.LoadingViewState
import kotlinx.android.synthetic.main.activity_genre_results.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class GenreResultsActivity : BaseActivity<GenreResultsViewModel>() {
    override val content: View get() = movies_list
    private lateinit var moviesAdapter: SearchResultsAdapter

    val viewModel: GenreResultsViewModel by viewModel { parametersOf(genreId()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_genre_results)

        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        moviesAdapter = SearchResultsAdapter(this, R.layout.item_movie_card_list)
        movies_list.adapter = moviesAdapter
        movies_list.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)

        viewModel.viewState.observe(this, this::updateView)
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

    private fun genreId(): String = intent.extras["GENRE_ID"] as String

    private fun updateView(viewState: LoadingViewState<SearchViewState>) {
        viewState.updateLayout(this) {
            moviesAdapter.setDataSource(it)
        }
    }
}
