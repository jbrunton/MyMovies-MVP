package com.jbrunton.mymovies.features.discover

import android.view.View
import android.widget.ProgressBar
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.jbrunton.mymovies.libs.kotterknife.bindView
import com.jbrunton.mymovies.libs.ui.controllers.BaseLoadingViewController

class DiscoverViewController(val viewModel: DiscoverViewModel) : BaseLoadingViewController<DiscoverViewState>() {
    override val contentView: View get() = view.findViewById(R.id.discover_content)

    val nowPlayingViewController by lazy { MoviesGridViewController(R.id.now_playing, viewModel::onMovieSelected) }
    val popularViewController by lazy { MoviesGridViewController(R.id.popular, viewModel::onMovieSelected) }
    val genreResultsViewController by lazy { MoviesGridViewController(R.id.genre_results, viewModel::onMovieSelected) }

    val genre_results: RecyclerView by bindView(R.id.genre_results)
    val genres: ChipGroup by bindView(R.id.genres)
    val genre_results_loading_indicator: ProgressBar by bindView(R.id.genre_results_loading_indicator)

    override fun onViewCreated(view: View) {
        super.onViewCreated(view)

        nowPlayingViewController.onViewCreated(view)
        popularViewController.onViewCreated(view)
        genreResultsViewController.onViewCreated(view)
    }

    override fun updateContentView(viewState: DiscoverViewState) {
        nowPlayingViewController.updateView(viewState.nowPlayingViewState)
        popularViewController.updateView(viewState.popularViewState)
        genreResultsViewController.updateView(viewState.genreResults)

        genres.removeAllViewsInLayout()
        genre_results.visibility = viewState.genreResultsVisibility

        viewState.genres.forEach { genre ->
            val chip = buildGenreChip(genre)
            genres.addView(chip)
        }

        genre_results_loading_indicator.visibility = viewState.genreResultsLoadingIndicatorVisibility

        if (viewState.scrollToGenreResults) {
            contentView.scrollTo(0, genres.bottom)
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
