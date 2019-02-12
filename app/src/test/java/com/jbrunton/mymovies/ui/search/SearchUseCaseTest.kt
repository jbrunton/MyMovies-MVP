package com.jbrunton.mymovies.ui.search

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.jbrunton.async.AsyncResult
import com.jbrunton.entities.models.Movie
import com.jbrunton.entities.repositories.MoviesRepository
import com.jbrunton.fixtures.MovieFactory
import com.jbrunton.mymovies.fixtures.InstantSchedulerRule
import com.jbrunton.mymovies.ui.search.SearchViewStateFactory.Companion.EmptyState
import com.jbrunton.mymovies.ui.shared.LoadingViewState
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Observable
import io.reactivex.observers.TestObserver
import io.reactivex.subjects.PublishSubject
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito

class SearchUseCaseTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var schedulerRule = InstantSchedulerRule()

    private lateinit var repository: MoviesRepository
    private lateinit var useCase: SearchUseCase
    private lateinit var observer: TestObserver<LoadingViewState<SearchViewState>>
    private lateinit var searches: PublishSubject<String>

    private val movieFactory = MovieFactory()

    private val MOVIE = movieFactory.create()
    private val LOADING_RESULT = AsyncResult.loading<List<Movie>>(null)
    private val SUCCESS_RESULT = AsyncResult.success(listOf(MOVIE))

    private val LOADING_VIEW_STATE = SearchViewStateFactory.from(LOADING_RESULT)
    private val SUCCESS_VIEW_STATE = SearchViewStateFactory.from(SUCCESS_RESULT)

    @Before
    fun setUp() {
        repository = Mockito.mock(MoviesRepository::class.java)
        useCase = SearchUseCase(repository)

        whenever(repository.searchMovies("Star"))
                .thenReturn(Observable.just(LOADING_RESULT, SUCCESS_RESULT))

        searches = PublishSubject.create()
        observer = useCase.start(searches).test()
    }

    @Test
    fun startsWithEmptyState() {
        observer.assertValue(EmptyState)
    }

    @Test
    fun showsEmptyStateForEmptyQuery() {
        searches.onNext("")
        observer.assertValues(EmptyState, EmptyState)
    }

    @Test
    fun searchesForQuery() {
        searches.onNext("Star")
        observer.assertValues(EmptyState, LOADING_VIEW_STATE, SUCCESS_VIEW_STATE)
    }
}
