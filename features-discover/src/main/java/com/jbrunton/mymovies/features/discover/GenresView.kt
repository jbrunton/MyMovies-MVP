package com.jbrunton.mymovies.features.discover

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.jbrunton.mymovies.libs.kotterknife.bindView

class GenresView(context: Context, attrs: AttributeSet): LinearLayout(context, attrs) {
    val genres: ChipGroup by bindView(R.id.genres)
    lateinit var listener: DiscoverListener

    init {
        inflate(context, R.layout.view_genres, this)
    }

    fun updateView(viewState: List<GenreChipViewState>) {
        genres.removeAllViewsInLayout()

        viewState.forEach { genre ->
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
}
