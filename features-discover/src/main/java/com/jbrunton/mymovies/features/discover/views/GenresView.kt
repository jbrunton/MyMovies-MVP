package com.jbrunton.mymovies.features.discover.views

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import com.jbrunton.mymovies.features.discover.interactor.DiscoverIntent
import com.jbrunton.mymovies.features.discover.interactor.DiscoverListener
import com.jbrunton.mymovies.features.discover.GenresViewState
import com.jbrunton.mymovies.features.discover.R
import kotlinx.android.synthetic.main.view_genres.view.*

class GenresView(context: Context, attrs: AttributeSet): LinearLayout(context, attrs) {
    private lateinit var listener: DiscoverListener

    init {
        inflate(context, R.layout.view_genres, this)
    }

    fun updateView(viewState: GenresViewState) {
        genre_results.updateView(viewState.genreResults)

        genre_results.visibility = viewState.genreResultsVisibility
        genre_results_loading_indicator.visibility = viewState.genreResultsLoadingIndicatorVisibility

        genres.visibility = viewState.genresVisibility
        genres.updateView(viewState.genres)

        selected_genre.text = viewState.selectedGenreText
        selected_genre.visibility = viewState.selectedGenreVisibility
    }

    fun setListener(listener: DiscoverListener) {
        this.listener = listener
        genres.setListener(listener)
        genre_results.setListener(listener)
        selected_genre.setOnCloseIconClickListener { listener.perform(DiscoverIntent.ClearSelectedGenre) }
    }
}
