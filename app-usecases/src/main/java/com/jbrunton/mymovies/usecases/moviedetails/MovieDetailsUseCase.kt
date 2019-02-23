package com.jbrunton.mymovies.usecases.moviedetails

import com.jbrunton.async.AsyncResult
import com.jbrunton.async.doOnError
import com.jbrunton.async.doOnSuccess
import com.jbrunton.async.map
import com.jbrunton.entities.SchedulerContext
import com.jbrunton.entities.errors.handleNetworkErrors
import com.jbrunton.entities.models.Movie
import com.jbrunton.entities.repositories.ApplicationPreferences
import com.jbrunton.entities.repositories.MoviesRepository
import com.jbrunton.entities.safelySubscribe
import com.jbrunton.mymovies.usecases.BaseUseCase
import io.reactivex.ObservableTransformer
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
        val preferences: ApplicationPreferences
) : BaseUseCase() {
    val snackbar = PublishSubject.create<MovieDetailsSnackbar>()
    val movie = PublishSubject.create<AsyncResult<MovieDetailsState>>()

    override fun start(schedulerContext: SchedulerContext) {
        super.start(schedulerContext)
        loadMovieDetails()
    }

    fun retry() = loadMovieDetails()

    fun favorite() {
        repository.favorite(movieId)
                .compose(favoriteHandler(this::handleFavoriteAdded))
                .safelySubscribe(this)
    }

    fun unfavorite() {
        repository.unfavorite(movieId)
                .compose(favoriteHandler(this::handleFavoriteRemoved))
                .safelySubscribe(this)
    }

    private fun loadMovieDetails() = fetchMovieDetails().safelySubscribe(this)

    private fun fetchMovieDetails() = repository.getMovie(movieId).map(this::handleMovieResult)

    private fun favoriteHandler(handler: (AsyncResult<Unit>) -> AsyncResult<Unit>) =
            ObservableTransformer<AsyncResult<Unit>, Unit> {
                it.map(handler).map(this::handleAuthFailure)
                        .flatMap { fetchMovieDetails() }
            }

    private fun handleMovieResult(result: AsyncResult<Movie>) {
        val state = result.handleNetworkErrors().map {
            val favorite = preferences.favorites.contains(movieId)
            MovieDetailsState(it, favorite)
        }
        movie.onNext(state)
    }

    private fun handleAuthFailure(result: AsyncResult<Unit>): AsyncResult<Unit> {
        return result.doOnError(HttpException::class) {
            action { snackbar.onNext(MovieDetailsSnackbar.SignedOut) } whenever { it.code() == 401 }
        }
    }

    private fun handleFavoriteAdded(result: AsyncResult<Unit>): AsyncResult<Unit> {
        return result.doOnSuccess { snackbar.onNext(MovieDetailsSnackbar.FavoriteAdded) }
    }

    private fun handleFavoriteRemoved(result: AsyncResult<Unit>): AsyncResult<Unit> {
        return result.doOnSuccess { snackbar.onNext(MovieDetailsSnackbar.FavoriteRemoved) }
    }
}