package com.jbrunton.mymovies.usecases.auth

import com.jbrunton.async.AsyncResult
import com.jbrunton.entities.SchedulerContext
import com.jbrunton.entities.models.AuthSession
import com.jbrunton.entities.repositories.AccountRepository
import com.jbrunton.fixtures.ImmediateSchedulerFactory
import com.jbrunton.fixtures.NetworkErrorFixtures.httpErrorResult
import com.jbrunton.fixtures.NetworkErrorFixtures.networkErrorResult
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Observable
import io.reactivex.observers.TestObserver
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

class LoginUseCaseTest {
    private lateinit var repository: AccountRepository
    private lateinit var useCase: LoginUseCase
    private lateinit var stateObserver: TestObserver<AsyncResult<LoginState>>
    private lateinit var successObserver: TestObserver<AuthSession>
    private lateinit var failureObserver: TestObserver<String>
    private lateinit var networkErrorSnackbarObserver: TestObserver<Unit>

    private val AUTH_SESSION = AuthSession("1234")
    private val LOADING_RESULT = AsyncResult.loading<AuthSession>(null)
    private val SUCCESS_RESULT = AsyncResult.success(AUTH_SESSION)
    private val AUTH_FAILURE_MESSAGE = "Some Error"
    private val AUTH_FAILURE_RESULT = httpErrorResult<AuthSession>(401, AUTH_FAILURE_MESSAGE)
    private val NETWORK_ERROR_RESULT = networkErrorResult<AuthSession>()

    private val LOADING_STATE = AsyncResult.loading(null)
    private val SUCCESS_STATE = AsyncResult.success(LoginState.Valid)

    private val INVALID_USERNAME_STATE = AsyncResult.success(LoginState.Invalid(requiresUsername = true, requiresPassword = false))
    private val INVALID_PASSWORD_STATE = AsyncResult.success(LoginState.Invalid(requiresUsername = false, requiresPassword = true))

    private val USERNAME = "myusername"
    private val PASSWORD = "mypassword"

    @Before
    fun setUp() {
        repository = Mockito.mock(AccountRepository::class.java)
        useCase = LoginUseCase(repository)

        successObserver = useCase.loginSuccessful.test()
        failureObserver = useCase.loginFailure.test()
        networkErrorSnackbarObserver = useCase.networkErrorSnackbar.test()
        stateObserver = useCase.state.test()

        useCase.start(SchedulerContext(ImmediateSchedulerFactory()))
    }

    @Test
    fun testSuccess() {
        stubLoginToReturn(SUCCESS_RESULT)

        useCase.login(USERNAME, PASSWORD)

        stateObserver.assertValues(LOADING_STATE, SUCCESS_STATE)
        successObserver.assertValue(AUTH_SESSION)
    }

    @Test
    fun testAuthFailure() {
        stubLoginToReturn(AUTH_FAILURE_RESULT)

        useCase.login(USERNAME, PASSWORD)

        stateObserver.assertValues(LOADING_STATE, SUCCESS_STATE)
        failureObserver.assertValue(AUTH_FAILURE_MESSAGE)
    }

    @Test
    fun testNetworkError() {
        stubLoginToReturn(NETWORK_ERROR_RESULT)

        useCase.login(USERNAME, PASSWORD)

        stateObserver.assertValues(LOADING_STATE, SUCCESS_STATE)
        networkErrorSnackbarObserver.assertValue(Unit)
    }

    @Test
    fun testInvalidUsername() {
        useCase.login("", PASSWORD)
        stateObserver.assertValues(INVALID_USERNAME_STATE)
    }

    @Test
    fun testInvalidPassword() {
        useCase.login(USERNAME, "")
        stateObserver.assertValues(INVALID_PASSWORD_STATE)
    }

    private fun stubLoginToReturn(result: AsyncResult<AuthSession>) {
        whenever(repository.login(USERNAME, PASSWORD))
                .thenReturn(Observable.just(LOADING_RESULT, result))
    }
}