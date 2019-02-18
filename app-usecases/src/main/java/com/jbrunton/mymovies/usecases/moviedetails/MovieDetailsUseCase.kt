package com.jbrunton.mymovies.usecases.moviedetails

import com.jbrunton.async.AsyncResult
import com.jbrunton.async.map
import com.jbrunton.entities.errors.handleNetworkErrors
import com.jbrunton.entities.models.Movie
import com.jbrunton.entities.repositories.ApplicationPreferences
import com.jbrunton.entities.repositories.DataStream
import com.jbrunton.entities.repositories.MoviesRepository

class MovieDetailsUseCase(
        val movieId: String,
        val repository: MoviesRepository,
        val preferences: ApplicationPreferences
) {
    fun movie(): DataStream<MovieDetailsState> {
        return repository.getMovie(movieId).map(this::handleResult)
    }

    fun favorite() {

    }

    fun unfavorite() {

    }

    private fun handleResult(result: AsyncResult<Movie>): AsyncResult<MovieDetailsState> {
        return result.handleNetworkErrors().map {
            val favorite = preferences.favorites.contains(movieId)
            MovieDetailsState(it, favorite)
        }
    }
}