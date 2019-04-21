package com.jbrunton.mymovies.ui.discover

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.chip.Chip
import com.jbrunton.libs.ui.BaseLoadingViewController
import com.jbrunton.mymovies.R
import com.jbrunton.mymovies.shared.ui.MoviesListAdapter
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.fragment_discover.*

class DiscoverViewController(val viewModel: DiscoverViewModel) : BaseLoadingViewController<DiscoverViewState>(), LayoutContainer {
    override val layout = R.layout.fragment_discover
    override val contentView: View get() = discover_content
    private lateinit var nowPlayingAdapter: MoviesListAdapter
    private lateinit var popularAdapter: MoviesListAdapter
    private lateinit var genreResultsAdapter: MoviesListAdapter

    override fun bind(containerView: View) {
        super.bind(containerView)
        nowPlayingAdapter = MoviesListAdapter(context, R.layout.item_movie_card_grid, viewModel::onMovieSelected)
        popularAdapter = MoviesListAdapter(context, R.layout.item_movie_card_grid, viewModel::onMovieSelected)
        genreResultsAdapter = MoviesListAdapter(context, R.layout.item_movie_card_grid, viewModel::onMovieSelected)

        now_playing.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        now_playing.adapter = nowPlayingAdapter

        popular.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        popular.adapter = popularAdapter

        genre_results.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        genre_results.adapter = genreResultsAdapter
    }

    override fun updateContentView(viewState: DiscoverViewState) {
        nowPlayingAdapter.setDataSource(viewState.nowPlayingViewState)
        popularAdapter.setDataSource(viewState.popularViewState)
        genreResultsAdapter.setDataSource(viewState.genreResults)

        genres.removeAllViewsInLayout()
        genre_results.visibility = viewState.genreResultsVisibility

        viewState.genres.forEach { genre ->
            val chip = buildGenreChip(genre)
            genres.addView(chip)
        }

        genre_results_loading_indicator.visibility = viewState.genreResultsLoadingIndicatorVisibility

        if (viewState.scrollToGenreResults) {
            discover_content.scrollTo(0, genres.bottom)
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