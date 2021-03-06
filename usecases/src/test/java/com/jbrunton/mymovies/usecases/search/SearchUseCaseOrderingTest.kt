package com.jbrunton.mymovies.usecases.search

import com.jbrunton.async.AsyncResult
import com.jbrunton.mymovies.entities.repositories.MoviesRepository
import com.jbrunton.mymovies.fixtures.MovieFactory
import com.jbrunton.mymovies.fixtures.RepositoryFixtures
import io.mockk.mockk
import io.reactivex.observers.TestObserver
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions
import org.junit.Before
import org.junit.Ignore
import org.junit.Test

@FlowPreview
@ExperimentalCoroutinesApi
class SearchUseCaseOrderingTest {
    //@get:Rule var schedulerRule = TestSchedulerRule()

    private lateinit var repository: MoviesRepository
    private lateinit var useCase: SearchUseCase
    private lateinit var observer: TestObserver<AsyncResult<SearchResult>>

    private val movieFactory = MovieFactory()

    private val MOVIE1 = movieFactory.create()
    private val MOVIE2 = movieFactory.create()
    private val MOVIE3 = movieFactory.create()

    private val EMPTY_QUERY_STATE = AsyncResult.success(SearchResult.EmptyQuery)
    private val MOVIE1_STATE = AsyncResult.success(SearchResult.Some(listOf(MOVIE1)))
    private val MOVIE3_STATE = AsyncResult.success(SearchResult.Some(listOf(MOVIE3)))

    @Before
    fun setUp() {
        repository = mockk()
        useCase = SearchUseCase(repository)

        //schedulerRule.TEST_SCHEDULER.triggerActions()

        // Note that movie 2 will take longer to arrive
        RepositoryFixtures.stubSearch(repository, "movie1").toReturnDelayed(listOf(MOVIE1), 1)
        RepositoryFixtures.stubSearch(repository, "movie2").toReturnDelayed(listOf(MOVIE2), 3)
        RepositoryFixtures.stubSearch(repository, "movie3").toReturnDelayed(listOf(MOVIE3), 1)
    }

    @Test
    @Ignore
    fun returnsResultsInOrder() {
        // first result arrives in order
        useCase.search("movie1")
        //schedulerRule.TEST_SCHEDULER.advanceTimeBy(3, TimeUnit.SECONDS)
        runBlocking {
            val states = useCase.results().toList()
            Assertions.assertThat(states).isEqualTo(listOf(EMPTY_QUERY_STATE, MOVIE1_STATE))
        }

        // second result takes longer to respond, isn't shown yet
        useCase.search("movie2")
        //schedulerRule.TEST_SCHEDULER.advanceTimeBy(1, TimeUnit.SECONDS)
        runBlocking {
            val states = useCase.results().toList()
            Assertions.assertThat(states).isEqualTo(listOf(EMPTY_QUERY_STATE, MOVIE1_STATE))
        }

        // third result arrives before second, so supersedes it
        useCase.search("movie3")
        //schedulerRule.TEST_SCHEDULER.advanceTimeBy(1, TimeUnit.SECONDS)
        runBlocking {
            val states = useCase.results().toList()
            Assertions.assertThat(states).isEqualTo(listOf(EMPTY_QUERY_STATE, MOVIE1_STATE, MOVIE3_STATE))
        }
    }
}
