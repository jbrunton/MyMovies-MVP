package com.jbrunton.mymovies.fixtures

import com.jbrunton.entities.Movie
import com.jbrunton.entities.MoviesRepository
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
            whenever(repository.getMovieRx(id)).thenReturn(Observable.just(movie).delay(delay.toLong(), TimeUnit.SECONDS))
        }

        fun toErrorWith(throwable: Throwable) {
            toErrorWithDelayed(throwable, 0)
        }

        fun toErrorWithDelayed(throwable: Throwable, delay: Int) {
            whenever(repository.getMovieRx(id)).thenReturn(Observable.error<Movie>(throwable).delay(delay.toLong(), TimeUnit.SECONDS))
        }
    }

    fun stubFind(repository: MoviesRepository, id: String): FakeMoviesFindDsl {
        return FakeMoviesFindDsl(repository, id)
    }
}
