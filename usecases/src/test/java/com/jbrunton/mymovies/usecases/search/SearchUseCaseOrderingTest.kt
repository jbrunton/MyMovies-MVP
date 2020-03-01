package com.jbrunton.mymovies.usecases.search

import com.jbrunton.async.AsyncResult
import com.jbrunton.mymovies.entities.repositories.MoviesRepository
import com.jbrunton.mymovies.fixtures.*
//import com.jbrunton.mymovies.fixtures.RepositoryFixtures
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.reactivex.observers.TestObserver
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.assertj.core.api.Assertions
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import java.util.concurrent.TimeUnit

@FlowPreview
@ExperimentalCoroutinesApi
class SearchUseCaseOrderingTest {
    @get:Rule val schedulerRule = TestSchedulerRule()
    @get:Rule val testDispatcherRule = TestDispatcherRule()

    @MockK private lateinit var repository: MoviesRepository
    private lateinit var useCase: SearchUseCase

    private val movieFactory = MovieFactory()

    private val MOVIE1 = movieFactory.create()
    private val MOVIE2 = movieFactory.create()
    private val MOVIE3 = movieFactory.create()

    private val EMPTY_QUERY_STATE = AsyncResult.success(SearchResult.EmptyQuery)
    private val MOVIE1_STATE = AsyncResult.success(SearchResult.Some(listOf(MOVIE1)))
    private val MOVIE3_STATE = AsyncResult.success(SearchResult.Some(listOf(MOVIE3)))

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        useCase = SearchUseCase(repository)

        schedulerRule.TEST_SCHEDULER.triggerActions()

        // Note that movie 2 will take longer to arrive
        runBlocking(testDispatcherRule.testDispatcher) {
            //RepositoryFixtures.stubSearch(repository, "movie1").toReturnDelayed(listOf(MOVIE1), 1)
            //RepositoryFixtures.stubSearch(repository, "movie2").toReturnDelayed(listOf(MOVIE2), 3)
            //RepositoryFixtures.stubSearch(repository, "movie3").toReturnDelayed(listOf(MOVIE3), 1)
        }
    }

    @Test
    fun returnsResultsInOrder() {
//        val flow1 = useCase.results()
//        val flow2 = useCase.results()
        runBlocking(testDispatcherRule.testDispatcher) {
            coEvery { repository.getMovie("movie1") } returns
                flowOf(AsyncResult.success(MOVIE1)).onEach { delay(1000) }

            launch { useCase.search("movie1") }

            // first result arrives in order
            testDispatcherRule.testDispatcher.advanceTimeBy(2000)
            useCase.results().test().assertValuesThenCancel(EMPTY_QUERY_STATE, MOVIE1_STATE)

            // second result takes longer to respond, isn't shown yet
//            launch { useCase.search("movie2") }
//            testDispatcherRule.testDispatcher.advanceTimeBy(1000)
//            flow2.test()
//                    .assertValuesThenCancel(EMPTY_QUERY_STATE, MOVIE1_STATE)
        }


        // third result arrives before second, so supersedes it
//        useCase.search("movie3")
//        schedulerRule.TEST_SCHEDULER.advanceTimeBy(1, TimeUnit.SECONDS)
//        runBlocking {
//            useCase.results().test()
//                    .assertValuesThenCancel(EMPTY_QUERY_STATE, MOVIE1_STATE, MOVIE3_STATE)
//        }
    }
}
