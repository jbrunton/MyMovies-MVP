package com.jbrunton.mymovies

import android.arch.lifecycle.ViewModelProvider
import com.jbrunton.entities.GenresRepository
import com.jbrunton.entities.MoviesRepository

interface ApplicationDependencies {
    val moviesRepository: MoviesRepository
    val genresRepository: GenresRepository
    val searchViewModelFactory: ViewModelProvider.Factory
}
