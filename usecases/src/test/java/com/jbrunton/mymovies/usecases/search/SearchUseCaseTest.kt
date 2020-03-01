package com.jbrunton.mymovies.usecases.search

import com.jbrunton.async.AsyncResult
import com.jbrunton.mymovies.entities.models.Movie
import com.jbrunton.mymovies.entities.repositories.MoviesRepository
import com.jbrunton.mymovies.fixtures.MainCoroutineScopeRule
import com.jbrunton.mymovies.fixtures.MovieFactory
import com.jbrunton.mymovies.fixtures.TestFlowCollector
import com.jbrunton.mymovies.fixtures.test
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.rx2.asFlowable
import kotlinx.coroutines.test.runBlockingTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@FlowPreview
@ExperimentalCoroutinesApi
class SearchUseCaseTest {
    @get:Rule val coroutineScope =  MainCoroutineScopeRule()

    @MockK private lateinit var repository: MoviesRepository
    private lateinit var useCase: SearchUseCase

    private val movieFactory = MovieFactory()

    private val MOVIE = movieFactory.create()
    private val LOADING_RESULT = AsyncResult.loading<List<Movie>>(null)
    private val SUCCESS_RESULT = AsyncResult.success(listOf(MOVIE))

    private val EmptyQueryState = AsyncResult.success(SearchResult.EmptyQuery)
    private val LoadingState = AsyncResult.loading(null)
    private val SuccessState = AsyncResult.success(SearchResult.Some(listOf(MOVIE)))

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        useCase = SearchUseCase(repository)

        coEvery { repository.searchMovies("Star") } returns
                flowOf(LOADING_RESULT, SUCCESS_RESULT)
    }

    @Test
    fun startsWithEmptyState() = runBlockingTest {
        useCase.results().test().assertValuesThenCancel(EmptyQueryState)
    }

    @Test
    fun showsEmptyStateForEmptyQuery() {
        runBlocking {
            launch { useCase.search("") }
            useCase.results().test().assertValuesThenCancel(EmptyQueryState, EmptyQueryState)
        }
    }

    @Test
    fun searchesForQuery() {
        runBlocking {
            launch { useCase.search("Star") }
            useCase.results().test()
                    .assertValuesThenCancel(EmptyQueryState, LoadingState, SuccessState)
        }
    }
}
