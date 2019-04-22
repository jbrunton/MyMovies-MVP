package com.jbrunton.mymovies.ui.discover

import android.view.View
import com.jbrunton.async.AsyncResult
import com.jbrunton.mymovies.libs.ui.LoadingViewState

object DiscoverViewStateFactory {
    fun viewState(result: AsyncResult<com.jbrunton.mymovies.usecases.discover.DiscoverState>): LoadingViewState<DiscoverViewState> {
        return LoadingViewState.build(DiscoverViewState.Empty).map(result) {
            val selectedGenre = it.selectedGenre
            val genres = selectedGenre?.let {
                listOf(GenreChipViewState(it, true))
            } ?: it.genres.map { GenreChipViewState(it, false) }
            DiscoverViewState(
                    nowPlayingViewState = it.nowPlaying.map { com.jbrunton.mymovies.shared.ui.MovieSearchResultViewState(it) },
                    popularViewState = it.popular.map { com.jbrunton.mymovies.shared.ui.MovieSearchResultViewState(it) },
                    genres = genres,
                    genreResultsVisibility = if (it.genreResults.isEmpty()) { View.GONE } else { View.VISIBLE },
                    genreResults = it.genreResults.map { com.jbrunton.mymovies.shared.ui.MovieSearchResultViewState(it) },
                    genreResultsLoadingIndicatorVisibility = if (it.selectedGenre != null && it.genreResults.isEmpty()) { View.VISIBLE } else { View.GONE },
                    scrollToGenreResults = !it.genreResults.isEmpty()
            )
        }
    }
}