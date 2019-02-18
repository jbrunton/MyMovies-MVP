package com.jbrunton.mymovies.usecases.moviedetails

import com.jbrunton.entities.models.Movie

data class MovieDetailsState(val movie: Movie, val favorite: Boolean)

