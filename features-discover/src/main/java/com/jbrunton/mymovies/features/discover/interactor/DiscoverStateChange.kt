package com.jbrunton.mymovies.features.discover.interactor

import com.jbrunton.async.AsyncResult
import com.jbrunton.mymovies.entities.models.Genre
import com.jbrunton.mymovies.entities.models.Movie
import com.jbrunton.mymovies.usecases.discover.DiscoverState

sealed class DiscoverStateChange {
    data class DiscoverResultsAvailable(val discoverResult: AsyncResult<DiscoverState>) : DiscoverStateChange()
    data class GenreSelected(val selectedGenre: Genre) : DiscoverStateChange()
    data class GenreResultsAvailable(val genreResults: AsyncResult<List<Movie>>) : DiscoverStateChange()
    object SelectedGenreCleared : DiscoverStateChange()
    object Nothing : DiscoverStateChange()
}