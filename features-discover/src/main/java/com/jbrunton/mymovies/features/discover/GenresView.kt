package com.jbrunton.mymovies.features.discover

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.jbrunton.mymovies.entities.models.Movie
import com.jbrunton.mymovies.libs.kotterknife.bindView
import kotlinx.android.synthetic.main.view_genres.view.*

class GenresView(context: Context, attrs: AttributeSet): LinearLayout(context, attrs) {
    lateinit var listener: DiscoverListener
    val genreResultsViewController by lazy { createGridViewController(R.id.genre_results) }

    init {
        inflate(context, R.layout.view_genres, this)
        genreResultsViewController.onViewCreated(this)
    }

    fun updateView(viewState: GenresViewState) {
        genreResultsViewController.updateView(viewState.genreResults)

        genre_results.visibility = viewState.genreResultsVisibility
        genre_results_loading_indicator.visibility = viewState.genreResultsLoadingIndicatorVisibility

        genres.removeAllViewsInLayout()

        viewState.genres.forEach { genre ->
            val chip = buildGenreChip(genre)
            genres.addView(chip)
        }
    }

    private fun buildGenreChip(viewState: GenreChipViewState): Chip {
        val chip = Chip(genres.context)
        chip.text = viewState.genre.name

        chip.setOnClickListener {
            listener.perform(DiscoverIntent.SelectGenre(viewState.genre))
        }

        if (viewState.selected) {
            chip.isCloseIconVisible = true
            chip.isSelected = true
            chip.setOnCloseIconClickListener {
                listener.perform(DiscoverIntent.ClearSelectedGenre)
            }
        }

        return chip
    }

    fun createGridViewController(listId: Int) = object : MoviesGridViewController() {
        override val recyclerView: RecyclerView by bindView(listId)
        override fun onMovieSelected(movie: Movie) = listener.perform(DiscoverIntent.SelectMovie(movie))
    }
}
