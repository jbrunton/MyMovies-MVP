package com.jbrunton.mymovies.usecases.moviedetails

import com.jbrunton.async.AsyncResult
import com.jbrunton.async.doOnSuccess
import com.jbrunton.async.map
import com.jbrunton.async.onError
import com.jbrunton.entities.errors.handleNetworkErrors
import com.jbrunton.entities.models.Movie
import com.jbrunton.entities.repositories.ApplicationPreferences
import com.jbrunton.entities.repositories.DataStream
import com.jbrunton.entities.repositories.MoviesRepository
import io.reactivex.subjects.PublishSubject
import retrofit2.HttpException

class MovieDetailsUseCase(
        val movieId: String,
        val repository: MoviesRepository,
        val preferences: ApplicationPreferences
) {
    val favoriteAddedSnackbar = PublishSubject.create<Unit>()
    val favoriteRemovedSnackbar = PublishSubject.create<Unit>()
    val signedOutSnackbar = PublishSubject.create<Unit>()

    fun movie(): DataStream<MovieDetailsState> {
        return repository.getMovie(movieId).map(this::handleResult)
    }

    fun favorite(): DataStream<Unit> {
        return repository.favorite(movieId)
                .map { result ->
                    result.doOnSuccess { favoriteAddedSnackbar.onNext(Unit) }
                    result.handleNetworkErrors().onError(HttpException::class) {
                        use(this@MovieDetailsUseCase::handleAuthFailure) whenever { it.code() == 401 }
                    }
                }
    }

    fun unfavorite(): DataStream<Unit> {
        return repository.unfavorite(movieId)
                .map { result ->
                    result.doOnSuccess { favoriteRemovedSnackbar.onNext(Unit) }
                    result.handleNetworkErrors().onError(HttpException::class) {
                        use(this@MovieDetailsUseCase::handleAuthFailure) whenever { it.code() == 401 }
                    }
                }
    }

    private fun handleAuthFailure(result: AsyncResult.Failure<Unit>) {
        signedOutSnackbar.onNext(Unit)
    }

    private fun handleResult(result: AsyncResult<Movie>): AsyncResult<MovieDetailsState> {
        return result.handleNetworkErrors().map {
            val favorite = preferences.favorites.contains(movieId)
            MovieDetailsState(it, favorite)
        }
    }
}