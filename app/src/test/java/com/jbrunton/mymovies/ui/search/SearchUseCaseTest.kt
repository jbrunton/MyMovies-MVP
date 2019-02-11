package com.jbrunton.mymovies.ui.search

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.jbrunton.entities.repositories.MoviesRepository
import com.jbrunton.fixtures.MovieFactory
import com.jbrunton.mymovies.fixtures.RepositoryFixtures
import com.jbrunton.mymovies.fixtures.TestSchedulerRule
import com.jbrunton.mymovies.ui.search.SearchViewStateFactory.Companion.EmptyState
import com.jbrunton.mymovies.ui.shared.LoadingViewState
import io.reactivex.observers.TestObserver
import io.reactivex.subjects.PublishSubject
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import java.util.concurrent.TimeUnit

class SearchUseCaseTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var schedulerRule = TestSchedulerRule()

    private lateinit var repository: MoviesRepository
    private lateinit var useCase: SearchUseCase
    private lateinit var observer: TestObserver<LoadingViewState<SearchViewState>>
    private lateinit var queries: PublishSubject<String>

    private val movieFactory = MovieFactory()
    private val MOVIE = movieFactory.create()
    private val SUCCESS_VIEW_STATE = LoadingViewState.success(SearchViewState.from(listOf(MOVIE)))

    @Before
    fun setUp() {
        repository = Mockito.mock(MoviesRepository::class.java)
        useCase = SearchUseCase(repository)
        RepositoryFixtures.stubSearch(repository, "Star").toReturnDelayed(listOf(MOVIE), 1)

        queries = PublishSubject.create()
    }

    @Test
    fun startsWithEmptyState() {
        startUseCase()
        observer.assertValue(EmptyState)
    }

    @Test
    fun showsEmptyStateForEmptyQuery() {
        startUseCase()

        queries.onNext("")

        schedulerRule.TEST_SCHEDULER.triggerActions()
        observer.assertValues(EmptyState, EmptyState)
    }

    @Test
    fun searchesForQuery() {
        startUseCase()

        queries.onNext("Star")

        schedulerRule.TEST_SCHEDULER.advanceTimeBy(1, TimeUnit.SECONDS)
        observer.assertValues(EmptyState, SUCCESS_VIEW_STATE)
    }

    private fun startUseCase() {
        observer = useCase.start(queries).test()
        schedulerRule.TEST_SCHEDULER.triggerActions()
    }
}
