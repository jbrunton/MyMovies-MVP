package com.jbrunton.mymovies.ui.discover

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.jbrunton.entities.models.Genre
import com.jbrunton.inject.injectViewModel
import com.jbrunton.mymovies.R
import com.jbrunton.mymovies.helpers.observe
import com.jbrunton.mymovies.ui.shared.BaseActivity
import com.jbrunton.mymovies.ui.shared.LoadingLayoutManager
import com.jbrunton.mymovies.ui.shared.LoadingViewState
import kotlinx.android.synthetic.main.activity_genres.*
import kotlinx.android.synthetic.main.layout_loading_state.*

class GenresActivity : BaseActivity<GenresViewModel>() {
    private lateinit var genresAdapter: GenresAdapter
    private lateinit var loadingLayoutManager: LoadingLayoutManager

    val viewModel: GenresViewModel by injectViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_genres)

        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        loadingLayoutManager = LoadingLayoutManager.buildFor(this, genres_list)

        genresAdapter = GenresAdapter(this)
        genres_list.adapter = genresAdapter

        viewModel.viewState.observe(this, this::updateView)
        viewModel.start()

        error_try_again.setOnClickListener { viewModel.retry() }
    }

    private fun updateView(viewState: LoadingViewState<GenresViewState>) {
        loadingLayoutManager.updateLayout(viewState, genresAdapter::addAll)
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
