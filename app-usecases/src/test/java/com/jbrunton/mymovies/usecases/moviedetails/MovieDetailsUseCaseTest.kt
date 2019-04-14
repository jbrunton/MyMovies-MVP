package com.jbrunton.mymovies.usecases.moviedetails

import com.jbrunton.async.AsyncResult
import com.jbrunton.entities.HasSchedulers
import com.jbrunton.entities.SchedulerContext
import com.jbrunton.entities.models.Movie
import com.jbrunton.entities.repositories.ApplicationPreferences
import com.jbrunton.entities.repositories.MoviesRepository
import com.jbrunton.fixtures.ImmediateSchedulerFactory
import com.jbrunton.fixtures.MovieFactory
import com.jbrunton.fixtures.NetworkErrorFixtures
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Observable
import io.reactivex.observers.TestObserver
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

class MovieDetailsUseCaseTest : HasSchedulers {
    override lateinit var schedulerContext: SchedulerContext
    private lateinit var repository: MoviesRepository
    private lateinit var useCase: MovieDetailsUseCase
    private lateinit var preferences: ApplicationPreferences
    private lateinit var viewStateObserver: TestObserver<AsyncResult<MovieDetails>>
    private lateinit var snackbarObserver: TestObserver<MovieDetailsSnackbar>

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
        useCase = MovieDetailsUseCase(MOVIE_ID, repository, preferences)

        snackbarObserver = useCase.snackbar.test()
        stubRepoToReturn(SUCCESS_RESULT)
        viewStateObserver = useCase.movie.test()

        useCase.start(SchedulerContext(ImmediateSchedulerFactory()))

    }

    @Test
    fun testSuccess() {
        viewStateObserver.assertValues(LOADING_STATE, SUCCESS_STATE)
    }

    @Test
    fun testFavorite() {
        whenever(repository.favorite(MOVIE_ID)).thenReturn(Observable.just(AsyncResult.success(Unit)))

        useCase.favorite()

        viewStateObserver.assertValues(LOADING_STATE, SUCCESS_STATE, LOADING_STATE, SUCCESS_STATE)
        snackbarObserver.assertValue(MovieDetailsSnackbar.FavoriteAdded)
    }

    @Test
    fun testUnfavorite() {
        whenever(repository.unfavorite(MOVIE_ID)).thenReturn(Observable.just(AsyncResult.success(Unit)))

        useCase.unfavorite()

        viewStateObserver.assertValues(LOADING_STATE, SUCCESS_STATE, LOADING_STATE, SUCCESS_STATE)
        snackbarObserver.assertValue(MovieDetailsSnackbar.FavoriteRemoved)
    }

    @Test
    fun testSignedOut() {
        whenever(repository.favorite(MOVIE_ID)).thenReturn(Observable.just(AUTH_FAILURE_RESULT))

        useCase.favorite()

        viewStateObserver.assertValues(LOADING_STATE, SUCCESS_STATE, LOADING_STATE, SUCCESS_STATE)
        snackbarObserver.assertValue(MovieDetailsSnackbar.SignedOut)
    }

    private fun stubRepoToReturn(result: AsyncResult<Movie>) {
        whenever(repository.getMovie(MOVIE_ID))
                .thenReturn(Observable.just(LOADING_RESULT, result))
    }
}