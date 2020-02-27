package com.jbrunton.mymovies.usecases.moviedetails

import com.jbrunton.async.AsyncResult
import com.jbrunton.mymovies.entities.HasSchedulers
import com.jbrunton.mymovies.entities.SchedulerContext
import com.jbrunton.mymovies.entities.models.Movie
import com.jbrunton.mymovies.entities.repositories.ApplicationPreferences
import com.jbrunton.mymovies.entities.repositories.MoviesRepository
import com.jbrunton.mymovies.fixtures.ImmediateSchedulerFactory
import com.jbrunton.mymovies.fixtures.MovieFactory
import com.jbrunton.mymovies.fixtures.NetworkErrorFixtures
import com.nhaarman.mockitokotlin2.whenever
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.reactivex.Observable
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

class MovieDetailsUseCaseTest : HasSchedulers {
    override lateinit var schedulerContext: SchedulerContext
    @MockK private lateinit var repository: MoviesRepository
    @MockK private lateinit var preferences: ApplicationPreferences
    private lateinit var useCase: MovieDetailsUseCase

    private val factory = MovieFactory()
    private val MOVIE = factory.create()
    private val MOVIE_ID = MOVIE.id

    private val LOADING_RESULT = AsyncResult.loading<Movie>(null)
    private val SUCCESS_RESULT = AsyncResult.success(MOVIE)
    private val AUTH_FAILURE_MESSAGE = "Some Error"
    private val AUTH_FAILURE_RESULT = NetworkErrorFixtures.httpErrorResult<Unit>(401, AUTH_FAILURE_MESSAGE)

    private val LOADING_STATE = AsyncResult.loading(null)
    private val SUCCESS_STATE = AsyncResult.success(MovieDetails(MOVIE, true))

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        schedulerContext = SchedulerContext(ImmediateSchedulerFactory())
        every { preferences.favorites } returns setOf(MOVIE_ID)
        useCase = MovieDetailsUseCase(repository, preferences)
    }

    @Test
    fun testSuccess() {
        runBlocking {
            coEvery { repository.getMovie(MOVIE_ID) } returns flowOf(LOADING_RESULT, SUCCESS_RESULT)
            val states = useCase.details(MOVIE_ID).toList()
            assertThat(states).isEqualTo(listOf(LOADING_STATE, SUCCESS_STATE))
        }
    }

    @Test
    fun testFavorite() {
        runBlocking {
            coEvery { repository.favorite(MOVIE_ID) } returns flowOf(AsyncResult.success(Unit))
            val states = useCase.favorite(MOVIE_ID).toList()
            assertThat(states).isEqualTo(listOf(AsyncResult.success(FavoriteResult.Success)))
        }
    }

    @Test
    fun testUnfavorite() {
        runBlocking {
            coEvery { repository.unfavorite(MOVIE_ID) } returns flowOf(AsyncResult.success(Unit))
            val states = useCase.unfavorite(MOVIE_ID).toList()
            assertThat(states).isEqualTo(listOf(AsyncResult.success(FavoriteResult.Success)))
        }
    }

    @Test
    fun testSignedOut() {
        runBlocking {
            coEvery { repository.favorite(MOVIE_ID) } returns flowOf(AUTH_FAILURE_RESULT)
            val states = useCase.favorite(MOVIE_ID).toList()
            assertThat(states).isEqualTo(listOf(AsyncResult.success(FavoriteResult.SignedOut)))
        }
    }
}