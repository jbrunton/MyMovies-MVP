package com.jbrunton.mymovies.features.discover

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.jbrunton.mymovies.entities.models.Movie
import com.jbrunton.mymovies.libs.kotterknife.bindView
import kotlinx.android.synthetic.main.view_genres.view.*

class GenresView(context: Context, attrs: AttributeSet): LinearLayout(context, attrs) {
    private lateinit var listener: DiscoverListener
    val genreResultsViewController by lazy { createGridViewController(R.id.genre_results) }

    init {
        inflate(context, R.layout.view_genres, this)
        genreResultsViewController.onViewCreated(this)
    }

    fun updateView(viewState: GenresViewState) {
        genreResultsViewController.updateView(viewState.genreResults)

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
        selected_genre.setOnCloseIconClickListener { listener.perform(DiscoverIntent.ClearSelectedGenre) }
    }

    fun createGridViewController(listId: Int) = object : MoviesGridViewController() {
        override val recyclerView: RecyclerView by bindView(listId)
        override fun onMovieSelected(movie: Movie) = listener.perform(DiscoverIntent.SelectMovie(movie))
    }
}
