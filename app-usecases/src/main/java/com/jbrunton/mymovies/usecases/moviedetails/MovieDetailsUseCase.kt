package com.jbrunton.mymovies.usecases.moviedetails

import com.jbrunton.async.AsyncResult
import com.jbrunton.async.doOnSuccess
import com.jbrunton.async.map
import com.jbrunton.async.onError
import com.jbrunton.entities.HasSchedulers
import com.jbrunton.entities.SchedulerContext
import com.jbrunton.entities.errors.handleNetworkErrors
import com.jbrunton.entities.models.Movie
import com.jbrunton.entities.repositories.ApplicationPreferences
import com.jbrunton.entities.repositories.MoviesRepository
import com.jbrunton.entities.subscribe
import io.reactivex.subjects.PublishSubject
import retrofit2.HttpException

class MovieDetailsUseCase(
        val movieId: String,
        val repository: MoviesRepository,
        val preferences: ApplicationPreferences,
        override val schedulerContext: SchedulerContext
) : HasSchedulers {
    val favoriteAddedSnackbar = PublishSubject.create<Unit>()
    val favoriteRemovedSnackbar = PublishSubject.create<Unit>()
    val signedOutSnackbar = PublishSubject.create<Unit>()
    val movie = PublishSubject.create<AsyncResult<MovieDetailsState>>()

    fun start() {
        subscribe(loadDetails())
    }

    fun favorite() {
        val observable = repository.favorite(movieId)
                .map { result ->
                    result.doOnSuccess { favoriteAddedSnackbar.onNext(Unit) }
                    result.handleNetworkErrors().onError(HttpException::class) {
                        use(this@MovieDetailsUseCase::handleAuthFailure) whenever { it.code() == 401 }
                    }
                }.flatMap { loadDetails() }
        subscribe(observable)
    }

    fun unfavorite() {
        val observable = repository.unfavorite(movieId)
                .map { result ->
                    result.doOnSuccess { favoriteRemovedSnackbar.onNext(Unit) }
                    result.handleNetworkErrors().onError(HttpException::class) {
                        use(this@MovieDetailsUseCase::handleAuthFailure) whenever { it.code() == 401 }
                    }
                }.flatMap { loadDetails() }
        subscribe(observable)
    }

    private fun loadDetails() = repository.getMovie(movieId).map(this::handleResult)

    private fun handleAuthFailure(result: AsyncResult.Failure<Unit>) {
        signedOutSnackbar.onNext(Unit)
    }

    private fun handleResult(result: AsyncResult<Movie>) {
        val state = result.handleNetworkErrors().map {
            val favorite = preferences.favorites.contains(movieId)
            MovieDetailsState(it, favorite)
        }
        movie.onNext(state)
    }
}