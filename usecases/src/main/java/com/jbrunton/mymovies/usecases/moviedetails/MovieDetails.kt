package com.jbrunton.mymovies.usecases.moviedetails

import com.jbrunton.mymovies.entities.models.Movie

data class MovieDetails(val movie: Movie, val favorite: Boolean)

