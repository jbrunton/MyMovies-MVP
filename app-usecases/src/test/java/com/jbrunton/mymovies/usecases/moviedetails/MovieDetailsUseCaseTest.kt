package com.jbrunton.mymovies.usecases.moviedetails

import com.jbrunton.async.AsyncResult
import com.jbrunton.entities.models.AuthSession
import com.jbrunton.entities.models.Movie
import com.jbrunton.entities.repositories.ApplicationPreferences
import com.jbrunton.entities.repositories.MoviesRepository
import com.jbrunton.fixtures.MovieFactory
import com.jbrunton.fixtures.NetworkErrorFixtures
import com.jbrunton.mymovies.usecases.auth.LoginState
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Observable
import io.reactivex.observers.TestObserver
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

class MovieDetailsUseCaseTest {
    private lateinit var repository: MoviesRepository
    private lateinit var useCase: MovieDetailsUseCase
    private lateinit var preferences: ApplicationPreferences
    private lateinit var viewStateObserver: TestObserver<AsyncResult<MovieDetailsState>>
    private lateinit var favoriteAddedObserver: TestObserver<Unit>
    private lateinit var favoriteRemovedObserver: TestObserver<Unit>

    private val factory = MovieFactory()
    private val MOVIE = factory.create()
    private val MOVIE_ID = MOVIE.id

    private val AUTH_SESSION = AuthSession("1234")
    private val LOADING_RESULT = AsyncResult.loading<Movie>(null)
    private val SUCCESS_RESULT = AsyncResult.success(MOVIE)
    private val AUTH_FAILURE_MESSAGE = "Some Error"
    private val AUTH_FAILURE_RESULT = NetworkErrorFixtures.httpErrorResult<AuthSession>(401, AUTH_FAILURE_MESSAGE)
    private val NETWORK_ERROR_RESULT = NetworkErrorFixtures.networkErrorResult<AuthSession>()

    private val LOADING_STATE = AsyncResult.loading(null)
    private val SUCCESS_STATE = AsyncResult.success(MovieDetailsState(MOVIE, true))

    private val INVALID_USERNAME_STATE = AsyncResult.success(LoginState.Invalid(requiresUsername = true, requiresPassword = false))
    private val INVALID_PASSWORD_STATE = AsyncResult.success(LoginState.Invalid(requiresUsername = false, requiresPassword = true))


    @Before
    fun setUp() {
        repository = Mockito.mock(MoviesRepository::class.java)
        preferences = Mockito.mock(ApplicationPreferences::class.java)
        whenever(preferences.favorites).thenReturn(setOf(MOVIE_ID))
        useCase = MovieDetailsUseCase(MOVIE_ID, repository, preferences)
        favoriteAddedObserver = useCase.favoriteAddedSnackbar.test()
        favoriteRemovedObserver = useCase.favoriteRemovedSnackbar.test()
    }

    @Test
    fun testSuccess() {
        stubRepoToReturn(SUCCESS_RESULT)
        viewStateObserver = useCase.movie().test()
        viewStateObserver.assertValues(LOADING_STATE, SUCCESS_STATE)
    }

    @Test
    fun testFavorite() {
        whenever(repository.favorite(MOVIE_ID)).thenReturn(Observable.just(Unit))

        val observer = useCase.favorite().test()

        observer.assertValue(Unit)
        favoriteAddedObserver.assertValue(Unit)
    }

    @Test
    fun testUnfavorite() {
        whenever(repository.unfavorite(MOVIE_ID)).thenReturn(Observable.just(Unit))

        val observer = useCase.unfavorite().test()

        observer.assertValue(Unit)
        favoriteRemovedObserver.assertValue(Unit)
    }

    private fun stubRepoToReturn(result: AsyncResult<Movie>) {
        whenever(repository.getMovie(MOVIE_ID))
                .thenReturn(Observable.just(LOADING_RESULT, result))
    }
}