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
    private lateinit var viewStateObserver: TestObserver<AsyncResult<MovieDetailsState>>
    private lateinit var favoriteAddedObserver: TestObserver<Unit>
    private lateinit var favoriteRemovedObserver: TestObserver<Unit>
    private lateinit var signedOutObserver: TestObserver<Unit>

    private val factory = MovieFactory()
    private val MOVIE = factory.create()
    private val MOVIE_ID = MOVIE.id

    private val LOADING_RESULT = AsyncResult.loading<Movie>(null)
    private val SUCCESS_RESULT = AsyncResult.success(MOVIE)
    private val AUTH_FAILURE_MESSAGE = "Some Error"
    private val AUTH_FAILURE_RESULT = NetworkErrorFixtures.httpErrorResult<Unit>(401, AUTH_FAILURE_MESSAGE)

    private val LOADING_STATE = AsyncResult.loading(null)
    private val SUCCESS_STATE = AsyncResult.success(MovieDetailsState(MOVIE, true))

    @Before
    fun setUp() {
        schedulerContext = SchedulerContext(ImmediateSchedulerFactory())
        repository = Mockito.mock(MoviesRepository::class.java)
        preferences = Mockito.mock(ApplicationPreferences::class.java)
        whenever(preferences.favorites).thenReturn(setOf(MOVIE_ID))
        useCase = MovieDetailsUseCase(MOVIE_ID, repository, preferences, schedulerContext)
        favoriteAddedObserver = useCase.favoriteAddedSnackbar.test()
        favoriteRemovedObserver = useCase.favoriteRemovedSnackbar.test()
        signedOutObserver = useCase.signedOutSnackbar.test()
        stubRepoToReturn(SUCCESS_RESULT)
        viewStateObserver = useCase.movie.test()
    }

    @Test
    fun testSuccess() {
        useCase.start()
        viewStateObserver.assertValues(LOADING_STATE, SUCCESS_STATE)
    }

    @Test
    fun testFavorite() {
        whenever(repository.favorite(MOVIE_ID)).thenReturn(Observable.just(AsyncResult.success(Unit)))

        useCase.favorite()

        viewStateObserver.assertValues(LOADING_STATE, SUCCESS_STATE)
        favoriteAddedObserver.assertValue(Unit)
    }

    @Test
    fun testUnfavorite() {
        whenever(repository.unfavorite(MOVIE_ID)).thenReturn(Observable.just(AsyncResult.success(Unit)))

        useCase.unfavorite()

        viewStateObserver.assertValues(LOADING_STATE, SUCCESS_STATE)
        favoriteRemovedObserver.assertValue(Unit)
    }

    @Test
    fun testSignedOut() {
        whenever(repository.favorite(MOVIE_ID)).thenReturn(Observable.just(AUTH_FAILURE_RESULT))

        useCase.favorite()

        viewStateObserver.assertValues(LOADING_STATE, SUCCESS_STATE)
        signedOutObserver.assertValue(Unit)
    }

    private fun stubRepoToReturn(result: AsyncResult<Movie>) {
        whenever(repository.getMovie(MOVIE_ID))
                .thenReturn(Observable.just(LOADING_RESULT, result))
    }
}