package com.jbrunton.mymovies.usecases.auth

import com.jbrunton.async.AsyncResult
import com.jbrunton.mymovies.entities.errors.networkError
import com.jbrunton.mymovies.entities.models.AuthSession
import com.jbrunton.mymovies.entities.repositories.AccountRepository
import com.jbrunton.mymovies.fixtures.NetworkErrorFixtures.httpErrorResult
import com.jbrunton.mymovies.fixtures.NetworkErrorFixtures.networkErrorResult
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Observable
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

class LoginUseCaseTest {
    private lateinit var repository: AccountRepository
    private lateinit var useCase: LoginUseCase

    private val AUTH_SESSION = AuthSession("1234")
    private val LOADING_RESULT = AsyncResult.loading<AuthSession>(null)
    private val SUCCESS_RESULT = AsyncResult.success(AUTH_SESSION)
    private val AUTH_FAILURE_MESSAGE = "Some Error"
    private val AUTH_FAILURE_RESULT = httpErrorResult<AuthSession>(401, AUTH_FAILURE_MESSAGE)
    private val NETWORK_ERROR_RESULT = networkErrorResult<AuthSession>()

    private val LOADING_STATE = AsyncResult.loading(null)
    private val SUCCESS_STATE = AsyncResult.success(LoginResult.SignedIn(AUTH_SESSION))
    private val NETWORK_ERROR_STATE = AsyncResult.failure(networkError(), null)
    private val AUTH_FAILURE_STATE = AsyncResult.failure(AUTH_FAILURE_RESULT.error, null)

    private val INVALID_USERNAME_STATE = AsyncResult.success(LoginResult.Invalid(requiresUsername = true, requiresPassword = false))
    private val INVALID_PASSWORD_STATE = AsyncResult.success(LoginResult.Invalid(requiresUsername = false, requiresPassword = true))

    private val USERNAME = "myusername"
    private val PASSWORD = "mypassword"

    @Before
    fun setUp() {
        repository = Mockito.mock(AccountRepository::class.java)
        useCase = LoginUseCase(repository)
    }

    @Test
    fun testSuccess() {
        stubLoginToReturn(SUCCESS_RESULT)
        val observer = useCase.login(USERNAME, PASSWORD).test()
        observer.assertValues(LOADING_STATE, SUCCESS_STATE)
    }

    @Test
    fun testAuthFailure() {
        stubLoginToReturn(AUTH_FAILURE_RESULT)
        val observer = useCase.login(USERNAME, PASSWORD).test()
        observer.assertValues(LOADING_STATE, AUTH_FAILURE_STATE)
    }

    @Test
    fun testNetworkError() {
        stubLoginToReturn(NETWORK_ERROR_RESULT)
        val observer = useCase.login(USERNAME, PASSWORD).test()
        observer.assertValues(LOADING_STATE, NETWORK_ERROR_STATE)
    }

    @Test
    fun testInvalidUsername() {
        useCase.login("", PASSWORD).test()
                .assertValues(INVALID_USERNAME_STATE)
    }

    @Test
    fun testInvalidPassword() {
        useCase.login(USERNAME, "").test()
                .assertValues(INVALID_PASSWORD_STATE)
    }

    private fun stubLoginToReturn(result: AsyncResult<AuthSession>) {
        whenever(repository.login(USERNAME, PASSWORD))
                .thenReturn(Observable.just(LOADING_RESULT, result))
    }
}