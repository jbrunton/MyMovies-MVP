package com.jbrunton.mymovies.usecases.moviedetails

import com.jbrunton.async.AsyncResult
import com.jbrunton.async.doOnSuccess
import com.jbrunton.async.map
import com.jbrunton.async.onError
import com.jbrunton.entities.SchedulerContext
import com.jbrunton.entities.errors.handleNetworkErrors
import com.jbrunton.entities.models.Movie
import com.jbrunton.entities.repositories.ApplicationPreferences
import com.jbrunton.entities.repositories.MoviesRepository
import io.reactivex.subjects.PublishSubject
import retrofit2.HttpException

sealed class MovieDetailsSnackbar {
    object FavoriteAdded : MovieDetailsSnackbar()
    object FavoriteRemoved: MovieDetailsSnackbar()
    object SignedOut : MovieDetailsSnackbar()
}

class MovieDetailsUseCase(
        val movieId: String,
        val repository: MoviesRepository,
        val preferences: ApplicationPreferences,
        val schedulerContext: SchedulerContext
) {
    val snackbar = PublishSubject.create<MovieDetailsSnackbar>()
    val movie = PublishSubject.create<AsyncResult<MovieDetailsState>>()

    fun start() {
        schedulerContext.subscribe(fetchMovieDetails())
    }

    fun favorite() {
        val observable = repository.favorite(movieId)
                .map { result ->
                    result.doOnSuccess { snackbar.onNext(MovieDetailsSnackbar.FavoriteAdded) }
                    result.handleNetworkErrors().onError(HttpException::class) {
                        use(this@MovieDetailsUseCase::handleAuthFailure) whenever { it.code() == 401 }
                    }
                }.flatMap { fetchMovieDetails() }
        schedulerContext.subscribe(observable)
    }

    fun unfavorite() {
        val observable = repository.unfavorite(movieId)
                .map { result ->
                    result.doOnSuccess { snackbar.onNext(MovieDetailsSnackbar.FavoriteRemoved) }
                    result.handleNetworkErrors().onError(HttpException::class) {
                        use(this@MovieDetailsUseCase::handleAuthFailure) whenever { it.code() == 401 }
                    }
                }.flatMap { fetchMovieDetails() }
        schedulerContext.subscribe(observable)
    }

    private fun fetchMovieDetails() = repository.getMovie(movieId).map(this::updateMovieDetails)

    private fun handleAuthFailure(result: AsyncResult.Failure<Unit>) {
        snackbar.onNext(MovieDetailsSnackbar.SignedOut)
    }

    private fun updateMovieDetails(result: AsyncResult<Movie>) {
        val state = result.handleNetworkErrors().map {
            val favorite = preferences.favorites.contains(movieId)
            MovieDetailsState(it, favorite)
        }
        movie.onNext(state)
    }
}