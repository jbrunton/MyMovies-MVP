package com.jbrunton.mymovies.ui.movies

import com.jbrunton.entities.models.Movie

data class MovieSearchResultViewState(val movie: Movie) : BaseMovieViewState(movie)
