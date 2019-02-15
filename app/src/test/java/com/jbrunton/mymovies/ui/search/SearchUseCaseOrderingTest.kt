package com.jbrunton.mymovies.ui.search

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.jbrunton.async.AsyncResult
import com.jbrunton.entities.repositories.MoviesRepository
import com.jbrunton.mymovies.fixtures.MovieFactory
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

class SearchUseCaseOrderingTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var schedulerRule = TestSchedulerRule()

    private lateinit var repository: MoviesRepository
    private lateinit var useCase: SearchUseCase
    private lateinit var observer: TestObserver<LoadingViewState<SearchViewState>>
    private lateinit var searches: PublishSubject<String>

    private val movieFactory = MovieFactory()

    private val MOVIE1 = movieFactory.create()
    private val MOVIE2 = movieFactory.create()
    private val MOVIE3 = movieFactory.create()

    private val MOVIE1_VIEW_STATE = SearchViewStateFactory.from(AsyncResult.success(listOf(MOVIE1)))
    private val MOVIE2_VIEW_STATE = SearchViewStateFactory.from(AsyncResult.success(listOf(MOVIE2)))
    private val MOVIE3_VIEW_STATE = SearchViewStateFactory.from(AsyncResult.success(listOf(MOVIE3)))

    @Before
    fun setUp() {
        repository = Mockito.mock(MoviesRepository::class.java)
        useCase = SearchUseCase(repository)
        searches = PublishSubject.create()
        observer = useCase.start(searches).test()

        // Note that movie 2 will take longer to arrive
        RepositoryFixtures.stubSearch(repository, "movie1").toReturnDelayed(listOf(MOVIE1), 1)
        RepositoryFixtures.stubSearch(repository, "movie2").toReturnDelayed(listOf(MOVIE2), 3)
        RepositoryFixtures.stubSearch(repository, "movie3").toReturnDelayed(listOf(MOVIE3), 1)
    }

    @Test
    fun returnsResultsInOrder() {
        // first result arrives in order
        searches.onNext("movie1")
        schedulerRule.TEST_SCHEDULER.advanceTimeBy(1, TimeUnit.SECONDS)
        observer.assertValues(EmptyState, MOVIE1_VIEW_STATE)

        // second result takes longer to respond, isn't shown yet
        searches.onNext("movie2")
        schedulerRule.TEST_SCHEDULER.advanceTimeBy(1, TimeUnit.SECONDS)
        observer.assertValues(EmptyState, MOVIE1_VIEW_STATE)

        // third result arrives before second, so supersedes it
        searches.onNext("movie3")
        schedulerRule.TEST_SCHEDULER.advanceTimeBy(1, TimeUnit.SECONDS)
        observer.assertValues(EmptyState, MOVIE1_VIEW_STATE, MOVIE3_VIEW_STATE)
    }
}
