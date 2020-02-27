package com.jbrunton.mymovies.fixtures

import com.jbrunton.async.AsyncResult
import com.jbrunton.mymovies.entities.models.Movie
import com.jbrunton.mymovies.entities.repositories.MoviesRepository
import com.nhaarman.mockitokotlin2.whenever
import io.mockk.coEvery
import io.mockk.every
import io.reactivex.Observable
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onEach
import java.util.concurrent.TimeUnit

object RepositoryFixtures {
    open class FakeMoviesRepositoryDsl constructor(protected val repository: MoviesRepository)

    class FakeMoviesFindDsl(repository: MoviesRepository, private val id: String) : FakeMoviesRepositoryDsl(repository) {
        suspend fun toReturn(movie: Movie) {
            toReturnDelayed(movie, 0)
        }

        suspend fun toReturnDelayed(movie: Movie, delayInSeconds: Int) {
            val result: AsyncResult<Movie> = AsyncResult.Success(movie)
            coEvery { repository.getMovie(id) } returns
                    flowOf(result).onEach { delay(delayInSeconds.toLong() * 1000) }
        }

        fun toErrorWith(throwable: Throwable) {
            toErrorWithDelayed(throwable, 0)
        }

        fun toErrorWithDelayed(throwable: Throwable, delayInSeconds: Int) {
            val result: AsyncResult<Movie> = AsyncResult.Failure(throwable, null)
            coEvery { repository.getMovie(id) } returns
                    flowOf(result).onEach { delay(delayInSeconds.toLong() * 1000) }
        }
    }

    class FakeMoviesSearchDsl(repository: MoviesRepository, private val query: String) : FakeMoviesRepositoryDsl(repository) {
        fun toReturn(movies: List<Movie>) {
            toReturnDelayed(movies, 0)
        }

        fun toReturnDelayed(movies: List<Movie>, delay: Int) {
            val result: AsyncResult<List<Movie>> = AsyncResult.Success(movies)
            every { repository.searchMovies(query) } returns Observable.just(result).delay(delay.toLong(), TimeUnit.SECONDS)
        }

        fun toErrorWith(throwable: Throwable) {
            toErrorWithDelayed(throwable, 0)
        }

        fun toErrorWithDelayed(throwable: Throwable, delay: Int) {
            val result: AsyncResult<List<Movie>> = AsyncResult.Failure(throwable, null)
            every { repository.searchMovies(query) } returns Observable.just(result).delay(delay.toLong(), TimeUnit.SECONDS)
        }
    }

    fun stubFind(repository: MoviesRepository, id: String): FakeMoviesFindDsl {
        return FakeMoviesFindDsl(repository, id)
    }

    fun stubSearch(repository: MoviesRepository, query: String): FakeMoviesSearchDsl {
        return FakeMoviesSearchDsl(repository, query)
    }
}