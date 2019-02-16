package com.jbrunton.mymovies.ui.discover

import com.jbrunton.async.AsyncResult
import com.jbrunton.async.map
import com.jbrunton.entities.errors.handleNetworkErrors
import com.jbrunton.entities.models.Genre
import com.jbrunton.entities.models.Movie
import com.jbrunton.mymovies.ui.movies.MovieSearchResultViewState
import com.jbrunton.mymovies.ui.shared.LoadingViewState
import com.jbrunton.mymovies.ui.shared.toLoadingViewState

class DiscoverViewStateFactory {
    companion object {
        fun map(
                nowPlaying: AsyncResult<List<Movie>>,
                popular: AsyncResult<List<Movie>>,
                genres: AsyncResult<List<Genre>>
        ): AsyncResult<DiscoverViewState> {
            return AsyncResult.zip(
                    convertList(nowPlaying),
                    convertList(popular),
                    genres.handleNetworkErrors(),
                    ::DiscoverViewState
            )
        }

        fun from(
                nowPlaying: AsyncResult<List<Movie>>,
                popular: AsyncResult<List<Movie>>,
                genres: AsyncResult<List<Genre>>
        ): LoadingViewState<DiscoverViewState> {
            return map(nowPlaying, popular, genres)
                    .toLoadingViewState(DiscoverViewState.Empty)
        }

        private fun convertList(result: AsyncResult<List<Movie>>) =
            result.map({ movies -> movies.map { MovieSearchResultViewState(it) } })
                    .handleNetworkErrors()
    }
}