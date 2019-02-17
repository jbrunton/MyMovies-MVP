package com.jbrunton.mymovies.usecases.auth

import com.jbrunton.async.AsyncResult
import com.jbrunton.entities.models.AuthSession
import com.jbrunton.entities.repositories.AccountRepository
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Observable
import io.reactivex.observers.TestObserver
import okhttp3.MediaType
import okhttp3.ResponseBody
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

class LoginUseCaseTest {
    private lateinit var repository: AccountRepository
    private lateinit var useCase: LoginUseCase
    private lateinit var viewStateObserver: TestObserver<AsyncResult<LoginState>>
    private lateinit var successObserver: TestObserver<AuthSession>
    private lateinit var failureObserver: TestObserver<String>
    private lateinit var retrySnackbarObserver: TestObserver<Unit>

    private val AUTH_SESSION = AuthSession("1234")
    private val FAILURE_MESSAGE = "Some Error"
    private val LOADING_RESULT = AsyncResult.loading<AuthSession>(null)
    private val SUCCESS_RESULT = AsyncResult.success(AUTH_SESSION)
    private val errorBody = ResponseBody.create(MediaType.parse("application/json"), "{ \"status_message\": \"Some Error\" }")
    private val FAILURE_RESULT = AsyncResult.failure<AuthSession>(HttpException(Response.error<Unit>(401, errorBody)))
    private val NETWORK_ERROR_RESULT = AsyncResult.failure<AuthSession>(IOException())

    private val LOADING_STATE = AsyncResult.loading(null)
    private val SUCCESS_STATE = AsyncResult.success(LoginState.Valid)

    private val USERNAME = "myusername"
    private val PASSWORD = "mypassword"

    @Before
    fun setUp() {
        repository = Mockito.mock(AccountRepository::class.java)
        useCase = LoginUseCase(repository)
        successObserver = useCase.loginSuccessful.test()
        failureObserver = useCase.loginFailure.test()
        retrySnackbarObserver = useCase.retrySnackbar.test()
    }

    @Test
    fun testSuccess() {
        whenever(repository.login(USERNAME, PASSWORD))
                .thenReturn(Observable.just(LOADING_RESULT, SUCCESS_RESULT))

        viewStateObserver = useCase.login(USERNAME, PASSWORD).test()

        viewStateObserver.assertValues(LOADING_STATE, SUCCESS_STATE)
        successObserver.assertValue(AUTH_SESSION)
    }

    @Test
    fun testAuthFailure() {
        whenever(repository.login(USERNAME, PASSWORD))
                .thenReturn(Observable.just(LOADING_RESULT, FAILURE_RESULT))

        viewStateObserver = useCase.login(USERNAME, PASSWORD).test()

        viewStateObserver.assertValues(LOADING_STATE, SUCCESS_STATE)
        failureObserver.assertValue(FAILURE_MESSAGE)
    }

    @Test
    fun testNetworkError() {
        whenever(repository.login(USERNAME, PASSWORD))
                .thenReturn(Observable.just(LOADING_RESULT, NETWORK_ERROR_RESULT))

        viewStateObserver = useCase.login(USERNAME, PASSWORD).test()

        viewStateObserver.assertValues(LOADING_STATE, SUCCESS_STATE)
        retrySnackbarObserver.assertValue(Unit)
    }
}