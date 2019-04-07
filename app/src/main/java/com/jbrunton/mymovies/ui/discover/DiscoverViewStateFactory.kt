package com.jbrunton.mymovies.ui.discover

import android.view.View
import com.jbrunton.async.AsyncResult
import com.jbrunton.mymovies.ui.movies.MovieSearchResultViewState
import com.jbrunton.mymovies.ui.shared.LoadingViewState

object DiscoverViewStateFactory {
    fun viewState(result: AsyncResult<com.jbrunton.mymovies.usecases.discover.DiscoverState>): LoadingViewState<DiscoverViewState> {
        return LoadingViewState.build(DiscoverViewState.Empty).map(result) {
            val selectedGenre = it.selectedGenre
            val genres = selectedGenre?.let {
                listOf(GenreChipViewState(it, true))
            } ?: it.genres.map { GenreChipViewState(it, false) }
            DiscoverViewState(
                    nowPlayingViewState = it.nowPlaying.map { MovieSearchResultViewState(it) },
                    popularViewState = it.popular.map { MovieSearchResultViewState(it) },
                    genres = genres,
                    genreResultsVisibility = if (it.genreResults.isEmpty()) { View.GONE } else { View.VISIBLE },
                    genreResults = it.genreResults.map { MovieSearchResultViewState(it) },
                    genreResultsLoadingIndicatorVisibility = if (it.selectedGenre != null && it.genreResults.isEmpty()) { View.VISIBLE } else { View.GONE },
                    scrollToGenreResults = !it.genreResults.isEmpty()
            )
        }
    }
}