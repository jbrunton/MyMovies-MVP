package com.jbrunton.mymovies.features.discover

import android.view.View
import android.widget.ProgressBar
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.ChipGroup
import com.jbrunton.mymovies.entities.models.Movie
import com.jbrunton.mymovies.libs.kotterknife.bindView
import com.jbrunton.mymovies.libs.ui.controllers.BaseLoadingViewController

class DiscoverViewController(val viewModel: DiscoverViewModel) : BaseLoadingViewController<DiscoverViewState>() {
    override val contentView: View get() = view.findViewById(R.id.discover_content)

    val nowPlayingViewController by lazy { createGridViewController(R.id.now_playing) }
    val popularViewController by lazy { createGridViewController(R.id.popular) }
    val genreResultsViewController by lazy { createGridViewController(R.id.genre_results) }
    val genreChipsViewController by lazy { GenreChipsViewController(viewModel) }

    val genre_results: RecyclerView by bindView(R.id.genre_results)
    val genres: ChipGroup by bindView(R.id.genres)
    val genre_results_loading_indicator: ProgressBar by bindView(R.id.genre_results_loading_indicator)

    override fun onViewCreated(view: View) {
        super.onViewCreated(view)

        nowPlayingViewController.onViewCreated(view)
        popularViewController.onViewCreated(view)
        genreResultsViewController.onViewCreated(view)
        genreChipsViewController.onViewCreated(view)
    }

    override fun updateContentView(viewState: DiscoverViewState) {
        nowPlayingViewController.updateView(viewState.nowPlayingViewState)
        popularViewController.updateView(viewState.popularViewState)
        genreResultsViewController.updateView(viewState.genreResults)
        genreChipsViewController.updateView(viewState.genres)

        genre_results.visibility = viewState.genreResultsVisibility
        genre_results_loading_indicator.visibility = viewState.genreResultsLoadingIndicatorVisibility

        if (viewState.scrollToGenreResults) {
            contentView.scrollTo(0, genres.bottom)
        }
    }

    fun createGridViewController(listId: Int) = object : MoviesGridViewController() {
        override val recyclerView: RecyclerView by bindView(listId)
        override fun onMovieSelected(movie: Movie) = viewModel.onMovieSelected(movie)
    }
}
