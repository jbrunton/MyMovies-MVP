package com.jbrunton.mymovies.usecases.moviedetails

import com.jbrunton.async.AsyncResult
import com.jbrunton.async.map
import com.jbrunton.entities.errors.handleNetworkErrors
import com.jbrunton.entities.models.Movie
import com.jbrunton.entities.repositories.ApplicationPreferences
import com.jbrunton.entities.repositories.DataStream
import com.jbrunton.entities.repositories.MoviesRepository
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

class MovieDetailsUseCase(
        val movieId: String,
        val repository: MoviesRepository,
        val preferences: ApplicationPreferences
) {
    val favoriteSuccessful = PublishSubject.create<Unit>()
    val unfavoriteSuccessful = PublishSubject.create<Unit>()

    fun movie(): DataStream<MovieDetailsState> {
        return repository.getMovie(movieId).map(this::handleResult)
    }

    fun favorite(): Observable<Unit> {
        return repository.favorite(movieId).doOnNext { favoriteSuccessful.onNext(Unit) }
    }

    fun unfavorite(): Observable<Unit> {
        return repository.unfavorite(movieId).doOnNext { unfavoriteSuccessful.onNext(Unit) }
    }

    private fun handleResult(result: AsyncResult<Movie>): AsyncResult<MovieDetailsState> {
        return result.handleNetworkErrors().map {
            val favorite = preferences.favorites.contains(movieId)
            MovieDetailsState(it, favorite)
        }
    }
}