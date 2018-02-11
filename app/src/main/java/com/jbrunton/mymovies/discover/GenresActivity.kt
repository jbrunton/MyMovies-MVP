package com.jbrunton.mymovies.discover

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.jbrunton.entities.Genre
import com.jbrunton.mymovies.R
import com.jbrunton.mymovies.helpers.observe
import com.jbrunton.mymovies.helpers.toVisibility
import com.jbrunton.mymovies.shared.BaseActivity
import kotlinx.android.synthetic.main.activity_genres.*

class GenresActivity : BaseActivity<GenresViewModel>() {
    private lateinit var genresAdapter: GenresAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_genres)

        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        genresAdapter = GenresAdapter(this)
        genres_list.adapter = genresAdapter

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

    override fun provideViewModel(): GenresViewModel {
        val factory = GenresViewModel.Factory(dependencies().genresRepository)
        return getViewModel(GenresViewModel::class.java, factory)
    }

    private fun updateView(viewState: GenresViewState) {
        genres_list.visibility = toVisibility(!viewState.loadingViewState().showError())
        genresAdapter.addAll(viewState.genres())

        updateLoadingView(viewState.loadingViewState())
    }

    protected class GenresAdapter(context: Context) : ArrayAdapter<Genre>(context, android.R.layout.simple_list_item_1) {

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            var convertView = convertView
            if (convertView == null) {
                // If there's no view to re-use, inflate a brand new view for row
                val inflater = LayoutInflater.from(context)
                convertView = inflater.inflate(android.R.layout.simple_list_item_1, parent, false)
            }

            val genre = getItem(position)

            val genreName = convertView!!.findViewById<View>(android.R.id.text1) as TextView
            genreName.setOnClickListener { view ->
                val intent = Intent(context, GenreResultsActivity::class.java)
                intent.putExtra("GENRE_ID", genre!!.id)
                context.startActivity(intent)
            }
            genreName.text = genre!!.name

            return convertView
        }
    }
}
