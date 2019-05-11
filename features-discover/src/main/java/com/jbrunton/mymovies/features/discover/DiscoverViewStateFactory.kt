package com.jbrunton.mymovies.features.discover

import android.view.View
import com.jbrunton.async.AsyncResult
import com.jbrunton.mymovies.entities.models.Genre
import com.jbrunton.mymovies.libs.ui.viewstates.LoadingViewState
import com.jbrunton.mymovies.shared.ui.MovieSearchResultViewState
import com.jbrunton.mymovies.usecases.discover.DiscoverResult

object DiscoverViewStateFactory {
    fun viewState(result: AsyncResult<DiscoverResult>): LoadingViewState<DiscoverViewState> {
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
            result: DiscoverResult,
            genres: List<Genre>
    ) = GenresViewState(
            genres = genres,
            genresVisibility = if (result.selectedGenre == null) View.VISIBLE else View.GONE,
            selectedGenreText = result.selectedGenre?.name ?: "",
            selectedGenreVisibility = if (result.selectedGenre == null) View.GONE else View.VISIBLE,
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