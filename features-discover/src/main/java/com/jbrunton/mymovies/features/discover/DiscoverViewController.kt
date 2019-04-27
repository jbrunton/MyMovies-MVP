package com.jbrunton.mymovies.features.discover

import android.view.View
import android.widget.ProgressBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.jbrunton.mymovies.libs.kotterknife.bindView
import com.jbrunton.mymovies.libs.ui.BaseLoadingViewController
import com.jbrunton.mymovies.shared.ui.MoviesListAdapter

class DiscoverViewController(val viewModel: DiscoverViewModel) : BaseLoadingViewController<DiscoverViewState>() {
    override val layout = R.layout.fragment_discover
    override val contentView: View get() = containerView.findViewById(R.id.discover_content)

    private lateinit var nowPlayingAdapter: MoviesListAdapter
    private lateinit var popularAdapter: MoviesListAdapter
    private lateinit var genreResultsAdapter: MoviesListAdapter

    private val now_playing: RecyclerView by bindView(R.id.now_playing)
    private val popular: RecyclerView by bindView(R.id.popular)
    private val genre_results: RecyclerView by bindView(R.id.genre_results)
    private val genres: ChipGroup by bindView(R.id.genres)
    private val genre_results_loading_indicator: ProgressBar by bindView(R.id.genre_results_loading_indicator)

    override fun initializeView(containerView: View) {
        super.initializeView(containerView)
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
