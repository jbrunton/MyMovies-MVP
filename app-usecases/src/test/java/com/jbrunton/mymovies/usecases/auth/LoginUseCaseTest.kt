package com.jbrunton.mymovies.usecases.auth

import com.jbrunton.async.AsyncResult
import com.jbrunton.entities.models.AuthSession
import com.jbrunton.entities.repositories.AccountRepository
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Observable
import io.reactivex.observers.TestObserver
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

class LoginUseCaseTest {
    private lateinit var repository: AccountRepository
    private lateinit var useCase: LoginUseCase
    private lateinit var viewStateObserver: TestObserver<AsyncResult<LoginState>>

    private val AUTH_SESSION = AuthSession("1234")
    private val LOADING_RESULT = AsyncResult.loading<AuthSession>(null)
    private val SUCCESS_RESULT = AsyncResult.success(AUTH_SESSION)

    private val LOADING_STATE = AsyncResult.loading(null)
    private val SUCCESS_STATE = AsyncResult.success(LoginState.Valid)

    private val USERNAME = "myusername"
    private val PASSWORD = "mypassword"

    @Before
    fun setUp() {
        repository = Mockito.mock(AccountRepository::class.java)
        useCase = LoginUseCase(repository)
    }

    @Test
    fun showsSuccessState() {
        whenever(repository.login(USERNAME, PASSWORD))
                .thenReturn(Observable.just(LOADING_RESULT, SUCCESS_RESULT))

        viewStateObserver = useCase.login(USERNAME, PASSWORD).test()
        viewStateObserver.assertValues(LOADING_STATE, SUCCESS_STATE)
    }
}