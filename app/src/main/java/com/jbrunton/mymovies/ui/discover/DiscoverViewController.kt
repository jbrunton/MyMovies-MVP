package com.jbrunton.mymovies.ui.discover

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.chip.Chip
import com.jbrunton.mymovies.R
import com.jbrunton.mymovies.ui.search.SearchResultsAdapter
import com.jbrunton.mymovies.ui.shared.BaseLoadingViewController
import kotlinx.android.synthetic.main.fragment_discover.*

class DiscoverViewController(val viewModel: DiscoverViewModel) : BaseLoadingViewController<DiscoverViewState>() {
    override val layout = R.layout.fragment_discover
    override val contentView: View get() = discover_content
    private lateinit var nowPlayingAdapter: SearchResultsAdapter
    private lateinit var popularAdapter: SearchResultsAdapter
    private lateinit var genreResultsAdapter: SearchResultsAdapter

    override fun bind(containerView: View) {
        super.bind(containerView)
        nowPlayingAdapter = SearchResultsAdapter(context, R.layout.item_movie_card_grid)
        popularAdapter = SearchResultsAdapter(context, R.layout.item_movie_card_grid)
        genreResultsAdapter = SearchResultsAdapter(context, R.layout.item_movie_card_grid)

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
        genres.visibility = viewState.genresVisibility
        genre_results.visibility = viewState.genreResultsVisibility

        viewState.genres.forEach { genre ->
            val chip = buildGenreChip(genre)
            genres.addView(chip)
        }

        genre_results_loading_indicator.visibility = viewState.genreResultsLoadingIndicatorVisibility
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