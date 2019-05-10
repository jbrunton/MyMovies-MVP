package com.jbrunton.mymovies.features.discover

import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.jbrunton.mymovies.libs.kotterknife.bindView
import com.jbrunton.mymovies.libs.ui.controllers.ViewController

class GenreChipsViewController(val viewModel: DiscoverViewModel) : ViewController<List<GenreChipViewState>>() {
    val genres: ChipGroup by bindView(R.id.genres)

    override fun updateView(viewState: List<GenreChipViewState>) {
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
            viewModel.onGenreClicked(viewState.genre)
        }

        if (viewState.selected) {
            chip.isCloseIconVisible = true
            chip.isSelected = true
            chip.setOnCloseIconClickListener {
                viewModel.onClearGenreSelection()
            }
        }

        return chip
    }
}