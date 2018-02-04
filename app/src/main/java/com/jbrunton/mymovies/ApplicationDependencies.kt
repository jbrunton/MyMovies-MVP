package com.jbrunton.mymovies

import com.jbrunton.entities.GenresRepository
import com.jbrunton.entities.MoviesRepository

interface ApplicationDependencies {
    val moviesRepository: MoviesRepository
    val genresRepository: GenresRepository
}
