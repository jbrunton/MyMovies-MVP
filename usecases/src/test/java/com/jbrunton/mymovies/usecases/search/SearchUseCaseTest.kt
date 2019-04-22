package com.jbrunton.mymovies.usecases.search

import com.jbrunton.async.AsyncResult
import com.jbrunton.mymovies.entities.models.Movie
import com.jbrunton.mymovies.entities.repositories.MoviesRepository
import com.jbrunton.mymovies.fixtures.ImmediateSchedulerFactory
import com.jbrunton.mymovies.fixtures.MovieFactory
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Observable
import io.reactivex.observers.TestObserver
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

class SearchUseCaseTest {
    private lateinit var repository: MoviesRepository
    private lateinit var useCase: SearchUseCase
    private lateinit var observer: TestObserver<AsyncResult<SearchState>>

    private val movieFactory = MovieFactory()

    private val MOVIE = movieFactory.create()
    private val LOADING_RESULT = AsyncResult.loading<List<Movie>>(null)
    private val SUCCESS_RESULT = AsyncResult.success(listOf(MOVIE))

    private val EmptyQueryState = AsyncResult.success(SearchState.EmptyQuery)
    private val LoadingState = AsyncResult.loading(null)
    private val SuccessState = AsyncResult.success(SearchState.Some(listOf(MOVIE)))

    @Before
    fun setUp() {
        repository = Mockito.mock(MoviesRepository::class.java)
        useCase = SearchUseCase(repository, ImmediateSchedulerFactory())

        whenever(repository.searchMovies("Star"))
                .thenReturn(Observable.just(LOADING_RESULT, SUCCESS_RESULT))

        observer = useCase.results().test()
    }

    @Test
    fun startsWithEmptyState() {
        observer.assertValue(EmptyQueryState)
    }

    @Test
    fun showsEmptyStateForEmptyQuery() {
        useCase.search("")
        observer.assertValues(EmptyQueryState, EmptyQueryState)
    }

    @Test
    fun searchesForQuery() {
        useCase.search("Star")
        observer.assertValues(EmptyQueryState, LoadingState, SuccessState)
    }
}
