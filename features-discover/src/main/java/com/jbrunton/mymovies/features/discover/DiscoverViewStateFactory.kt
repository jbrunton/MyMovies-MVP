package com.jbrunton.mymovies.features.discover

import android.view.View
import com.jbrunton.async.AsyncResult
import com.jbrunton.mymovies.libs.ui.viewstates.LoadingViewState
import com.jbrunton.mymovies.shared.ui.MovieSearchResultViewState
import com.jbrunton.mymovies.usecases.discover.DiscoverResult

object DiscoverViewStateFactory {
    fun viewState(result: AsyncResult<DiscoverResult>): LoadingViewState<DiscoverViewState> {
        return LoadingViewState.build(DiscoverViewState.Empty).map(result) {
            val genres = it.selectedGenre?.let {
                listOf(GenreChipViewState(it, true))
            } ?: it.genres.map { GenreChipViewState(it, false) }
            DiscoverViewState(
                    nowPlayingViewState = it.nowPlaying.map { MovieSearchResultViewState(it) },
                    popularViewState = it.popular.map { MovieSearchResultViewState(it) },
                    genresViewState = genresViewState(it, genres),
                    scrollToGenreResults = !it.genreResults.isEmpty()
            )
        }
    }

    private fun genresViewState(
            result: DiscoverResult,
            genres: List<GenreChipViewState>
    ) = GenresViewState(
            genres = genres,
            genreResultsVisibility = if (result.genreResults.isEmpty()) {
                View.GONE
            } else {
                View.VISIBLE
            },
            genreResults = result.genreResults.map { MovieSearchResultViewState(it) },
            genreResultsLoadingIndicatorVisibility = if (result.selectedGenre != null && result.genreResults.isEmpty()) {
                View.VISIBLE
            } else {
                View.GONE
            })
}