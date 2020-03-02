package com.jbrunton.mymovies.features.discover.views

import android.content.Context
import android.util.AttributeSet
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.jbrunton.mymovies.entities.models.Genre
import com.jbrunton.mymovies.features.discover.interactor.DiscoverIntent
import com.jbrunton.mymovies.features.discover.interactor.DiscoverListener
import kotlinx.android.synthetic.main.view_genres.view.*

class GenresChipGroup(context: Context, attrs: AttributeSet): ChipGroup(context, attrs) {
    private lateinit var listener: DiscoverListener

    fun setListener(listener: DiscoverListener) {
        this.listener = listener
    }

    fun updateView(genres: List<Genre>) {
        removeAllViewsInLayout()
        genres.forEach { genre ->
            val chip = buildGenreChip(genre)
            addView(chip)
        }
    }

    private fun buildGenreChip(genre: Genre): Chip {
        val chip = Chip(genres.context)
        chip.text = genre.name

        chip.setOnClickListener {
            listener.perform(DiscoverIntent.SelectGenre(genre))
        }

        return chip
    }
}