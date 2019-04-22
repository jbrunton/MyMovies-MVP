package com.jbrunton.mymovies.fixtures.repositories

import com.jbrunton.mymovies.entities.models.Movie
import com.jbrunton.mymovies.entities.repositories.MoviesRepository

fun MoviesRepository.stubSearch(query: String, results: List<Movie>) {
    (this as TestMoviesRepository).stubSearch(query, results)
}

fun MoviesRepository.stubWith(movies: List<Movie>) {
    (this as TestMoviesRepository).stubWith(movies)
}