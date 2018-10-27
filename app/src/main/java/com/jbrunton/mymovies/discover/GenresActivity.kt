package com.jbrunton.mymovies.discover

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.jbrunton.entities.Genre
import com.jbrunton.mymovies.R
import com.jbrunton.mymovies.helpers.observe
import com.jbrunton.mymovies.shared.BaseActivity
import kotlinx.android.synthetic.main.activity_genres.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class GenresActivity : BaseActivity<GenresViewModel>() {
    private lateinit var genresAdapter: GenresAdapter

    val viewModel: GenresViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_genres)

        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        genresAdapter = GenresAdapter(this)
        genres_list.adapter = genresAdapter

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

    private fun updateView(viewState: GenresViewState) {
        viewState.updateLayout(findViewById(android.R.id.content), content = genres_list) {
            genresAdapter.addAll(it)
        }
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
