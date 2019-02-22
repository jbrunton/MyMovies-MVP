package com.jbrunton.mymovies.usecases.search

import com.jbrunton.async.AsyncResult
import com.jbrunton.entities.SchedulerContext
import com.jbrunton.entities.models.Movie
import com.jbrunton.entities.repositories.MoviesRepository
import com.jbrunton.fixtures.ImmediateSchedulerFactory
import com.jbrunton.fixtures.MovieFactory
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

    private val EMPTY_QUERY_STATE = AsyncResult.success(SearchState.EmptyQuery)
    private val LOADING_STATE = AsyncResult.loading(null)
    private val SUCCESS_STATE = AsyncResult.success(SearchState.Some(listOf(MOVIE)))

    @Before
    fun setUp() {
        repository = Mockito.mock(MoviesRepository::class.java)
        useCase = SearchUseCase(repository)

        whenever(repository.searchMovies("Star"))
                .thenReturn(Observable.just(LOADING_RESULT, SUCCESS_RESULT))

        observer = useCase.results.test()
        useCase.start(SchedulerContext(ImmediateSchedulerFactory()))
    }

    @Test
    fun startsWithEmptyState() {
        observer.assertValue(EMPTY_QUERY_STATE)
    }

    @Test
    fun showsEmptyStateForEmptyQuery() {
        useCase.search("")
        observer.assertValues(EMPTY_QUERY_STATE, EMPTY_QUERY_STATE)
    }

    @Test
    fun searchesForQuery() {
        useCase.search("Star")
        observer.assertValues(EMPTY_QUERY_STATE, LOADING_STATE, SUCCESS_STATE)
    }
}
