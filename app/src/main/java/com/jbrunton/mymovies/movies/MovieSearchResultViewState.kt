package com.jbrunton.mymovies.movies

import com.jbrunton.entities.Movie

data class MovieSearchResultViewState(val movie: Movie) : BaseMovieViewState(movie)
