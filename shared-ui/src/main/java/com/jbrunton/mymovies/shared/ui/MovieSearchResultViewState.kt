package com.jbrunton.mymovies.shared.ui

import com.jbrunton.mymovies.entities.models.Movie

data class MovieSearchResultViewState(val movie: Movie) : BaseMovieViewState(movie)
