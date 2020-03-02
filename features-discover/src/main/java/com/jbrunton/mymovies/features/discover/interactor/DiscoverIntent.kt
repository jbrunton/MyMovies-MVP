package com.jbrunton.mymovies.features.discover.interactor

import com.jbrunton.mymovies.entities.models.Genre
import com.jbrunton.mymovies.entities.models.Movie

sealed class DiscoverIntent {
    object Load : DiscoverIntent()
    data class SelectGenre(val genre: Genre) : DiscoverIntent()
    data class SelectMovie(val movie: Movie) : DiscoverIntent()
    object ClearSelectedGenre : DiscoverIntent()
}