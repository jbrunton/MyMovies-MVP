package com.jbrunton.mymovies.fixtures

import com.jbrunton.entities.Movie
import com.jbrunton.entities.MoviesRepository

fun MoviesRepository.stubSearch(query: String, results: List<Movie>) {
    (this as TestMoviesRepository).stubSearch(query, results)
}