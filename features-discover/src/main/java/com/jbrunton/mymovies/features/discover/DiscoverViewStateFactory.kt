package com.jbrunton.mymovies.features.discover

import android.view.View
import com.jbrunton.async.AsyncResult
import com.jbrunton.mymovies.entities.models.Genre
import com.jbrunton.mymovies.libs.ui.viewstates.LoadingViewState
import com.jbrunton.mymovies.shared.ui.MovieSearchResultViewState
import com.jbrunton.mymovies.usecases.discover.DiscoverState

object DiscoverViewStateFactory {
    fun viewState(result: AsyncResult<DiscoverState>): LoadingViewState<DiscoverViewState> {
        return LoadingViewState.build(DiscoverViewState.Empty).map(result) {
            val genres = it.selectedGenre?.let { emptyList<Genre>() } ?: it.genres
            DiscoverViewState(
                    nowPlayingViewState = it.nowPlaying.map { MovieSearchResultViewState(it) },
                    popularViewState = it.popular.map { MovieSearchResultViewState(it) },
                    genresViewState = genresViewState(it, genres)
            )
        }
    }

    private fun genresViewState(
            state: DiscoverState,
            genres: List<Genre>
    ) = GenresViewState(
            genres = genres,
            genresVisibility = if (state.selectedGenre == null) View.VISIBLE else View.GONE,
            selectedGenreText = state.selectedGenre?.name ?: "",
            selectedGenreVisibility = if (state.selectedGenre == null) View.GONE else View.VISIBLE,
            genreResultsVisibility = if (state.genreResults.isEmpty()) {
                View.GONE
            } else {
                View.VISIBLE
            },
            genreResults = state.genreResults.map { MovieSearchResultViewState(it) },
            genreResultsLoadingIndicatorVisibility = if (state.selectedGenre != null && state.genreResults.isEmpty()) {
                View.VISIBLE
            } else {
                View.GONE
            })
}