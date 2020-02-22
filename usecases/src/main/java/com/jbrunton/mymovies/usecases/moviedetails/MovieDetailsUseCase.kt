package com.jbrunton.mymovies.usecases.moviedetails

import com.jbrunton.async.AsyncResult
import com.jbrunton.async.map
import com.jbrunton.async.onError
import com.jbrunton.mymovies.entities.errors.handleNetworkErrors
import com.jbrunton.mymovies.entities.models.Movie
import com.jbrunton.mymovies.entities.repositories.ApplicationPreferences
import com.jbrunton.mymovies.entities.repositories.DataStream
import com.jbrunton.mymovies.entities.repositories.FlowDataStream
import com.jbrunton.mymovies.entities.repositories.MoviesRepository
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.rx2.asObservable
import retrofit2.HttpException

sealed class FavoriteResult {
    object Success : FavoriteResult()
    object SignedOut : FavoriteResult()
}

class MovieDetailsUseCase(
        val repository: MoviesRepository,
        val preferences: ApplicationPreferences
) {
    suspend fun details(movieId: String): FlowDataStream<MovieDetails> = coroutineScope {
        repository.getMovie(movieId)
                .map { handleMovieResult(it) }
    }

    suspend fun favorite(movieId: String): FlowDataStream<FavoriteResult> {
        return repository.favorite(movieId)
                .map { handleFavoriteResult(it) }
    }

    suspend fun unfavorite(movieId: String): FlowDataStream<FavoriteResult> {
        return repository.unfavorite(movieId)
                .map { handleFavoriteResult(it) }
    }

    private fun handleMovieResult(result: AsyncResult<Movie>): AsyncResult<MovieDetails> {
        return result.handleNetworkErrors().map {
            val favorite = preferences.favorites.contains(it.id)
            MovieDetails(it, favorite)
        }
    }

    private fun handleFavoriteResult(result: AsyncResult<Unit>): AsyncResult<FavoriteResult> {
        return result.handleNetworkErrors()
                .map { FavoriteResult.Success as FavoriteResult }
                .onError(HttpException::class) {
                    use { FavoriteResult.SignedOut } whenever { it.code() == 401 }
                }
    }
}