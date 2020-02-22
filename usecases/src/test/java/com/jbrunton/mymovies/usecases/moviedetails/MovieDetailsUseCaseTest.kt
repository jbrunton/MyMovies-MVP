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
import io.reactivex.Observable
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

class MovieDetailsUseCaseTest : HasSchedulers {
    override lateinit var schedulerContext: SchedulerContext
    private lateinit var repository: MoviesRepository
    private lateinit var useCase: MovieDetailsUseCase
    private lateinit var preferences: ApplicationPreferences

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
        schedulerContext = SchedulerContext(ImmediateSchedulerFactory())
        repository = Mockito.mock(MoviesRepository::class.java)
        preferences = Mockito.mock(ApplicationPreferences::class.java)
        whenever(preferences.favorites).thenReturn(setOf(MOVIE_ID))
        useCase = MovieDetailsUseCase(repository, preferences)
        stubRepoToReturn(SUCCESS_RESULT)
    }

    @Test
    fun testSuccess() {
        useCase.details(MOVIE_ID).test()
                .assertValues(LOADING_STATE, SUCCESS_STATE)
    }

    @Test
    fun testFavorite() {
        whenever(repository.favorite(MOVIE_ID)).thenReturn(Observable.just(AsyncResult.success(Unit)))

        useCase.favorite(MOVIE_ID).test()
                .assertValues(AsyncResult.success(FavoriteResult.Success))
    }

    @Test
    fun testUnfavorite() {
        whenever(repository.unfavorite(MOVIE_ID)).thenReturn(Observable.just(AsyncResult.success(Unit)))

        useCase.unfavorite(MOVIE_ID).test()
                .assertValues(AsyncResult.success(FavoriteResult.Success))
    }

    @Test
    fun testSignedOut() {
        whenever(repository.favorite(MOVIE_ID)).thenReturn(Observable.just(AUTH_FAILURE_RESULT))

        useCase.favorite(MOVIE_ID).test()
                .assertValues(AsyncResult.success(FavoriteResult.SignedOut))
    }

    private fun stubRepoToReturn(result: AsyncResult<Movie>) {
        whenever(repository.getMovie(MOVIE_ID))
                .thenReturn(Observable.just(LOADING_RESULT, result))
    }
}