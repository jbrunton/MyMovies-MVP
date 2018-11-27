package com.jbrunton.mymovies.fixtures

import com.jbrunton.async.AsyncResult
import com.jbrunton.entities.models.Movie
import com.jbrunton.entities.repositories.MoviesRepository
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Observable
import java.util.concurrent.TimeUnit

object RepositoryFixtures {
    open class FakeMoviesRepositoryDsl constructor(protected val repository: MoviesRepository)

    class FakeMoviesFindDsl(repository: MoviesRepository, private val id: String) : FakeMoviesRepositoryDsl(repository) {

        fun toReturn(movie: Movie) {
            toReturnDelayed(movie, 0)
        }

        fun toReturnDelayed(movie: Movie, delay: Int) {
            val result: AsyncResult<Movie> = AsyncResult.Success(movie)
            whenever(repository.getMovie(id)).thenReturn(Observable.just(result).delay(delay.toLong(), TimeUnit.SECONDS))
        }

        fun toErrorWith(throwable: Throwable) {
            toErrorWithDelayed(throwable, 0)
        }

        fun toErrorWithDelayed(throwable: Throwable, delay: Int) {
            val result: AsyncResult<Movie> = AsyncResult.Failure(throwable, null)
            whenever(repository.getMovie(id)).thenReturn(Observable.just(result).delay(delay.toLong(), TimeUnit.SECONDS))
        }
    }

    fun stubFind(repository: MoviesRepository, id: String): FakeMoviesFindDsl {
        return FakeMoviesFindDsl(repository, id)
    }
}
