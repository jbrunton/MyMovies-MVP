package com.jbrunton.mymovies.ui.discover

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.chip.Chip
import com.jbrunton.entities.models.Genre
import com.jbrunton.mymovies.R
import com.jbrunton.mymovies.usecases.nav.NavigationRequest
import com.jbrunton.mymovies.nav.Navigator
import com.jbrunton.mymovies.ui.search.SearchResultsAdapter
import com.jbrunton.mymovies.ui.shared.BaseLoadingViewController
import kotlinx.android.synthetic.main.fragment_discover.*

class DiscoverViewController(val navigator: Navigator) : BaseLoadingViewController<DiscoverViewState>() {
    override val layout = R.layout.fragment_discover
    override val contentView: View get() = discover_content
    private lateinit var nowPlayingAdapter: SearchResultsAdapter
    private lateinit var popularAdapter: SearchResultsAdapter

    override fun bind(containerView: View) {
        super.bind(containerView)
        nowPlayingAdapter = SearchResultsAdapter(context, R.layout.item_movie_card_grid)
        popularAdapter = SearchResultsAdapter(context, R.layout.item_movie_card_grid)

        now_playing.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        now_playing.adapter = nowPlayingAdapter

        popular.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        popular.adapter = popularAdapter
    }

    override fun updateContentView(viewState: DiscoverViewState) {
        nowPlayingAdapter.setDataSource(viewState.nowPlayingViewState)
        popularAdapter.setDataSource(viewState.popularViewState)

        genres.removeAllViewsInLayout()
        viewState.genres.forEach { genre ->
            val chip = buildGenreChip(genre)
            genres.addView(chip)
        }
    }

    private fun buildGenreChip(genre: Genre): Chip {
        val chip = Chip(genres.context)
        chip.text = genre.name
        chip.setOnClickListener {
            navigator.navigate(NavigationRequest.GenreRequest(genre))
        }
        return chip
    }
}