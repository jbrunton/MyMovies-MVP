package com.jbrunton.mymovies.usecases.auth

import com.jbrunton.async.AsyncResult
import com.jbrunton.mymovies.entities.errors.networkError
import com.jbrunton.mymovies.entities.models.AuthSession
import com.jbrunton.mymovies.entities.repositories.AccountRepository
import com.jbrunton.mymovies.fixtures.MainCoroutineScopeRule
import com.jbrunton.mymovies.fixtures.NetworkErrorFixtures.httpErrorResult
import com.jbrunton.mymovies.fixtures.NetworkErrorFixtures.networkErrorResult
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runBlockingTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class LoginUseCaseTest {
    @get:Rule val coroutineScope =  MainCoroutineScopeRule()

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
        repository = mockk()
        useCase = LoginUseCase(repository)
    }

    @Test
    fun testSuccess() = runBlockingTest {
        stubLoginToReturn(SUCCESS_RESULT)
        val states = useCase.login(USERNAME, PASSWORD).toList()
        assertThat(states).isEqualTo(listOf(LOADING_STATE, SUCCESS_STATE))
    }

    @Test
    fun testAuthFailure() = runBlockingTest {
        stubLoginToReturn(AUTH_FAILURE_RESULT)
        val states = useCase.login(USERNAME, PASSWORD).toList()
        assertThat(states).isEqualTo(listOf(LOADING_STATE, AUTH_FAILURE_STATE))
    }

    @Test
    fun testNetworkError() = runBlockingTest {
        stubLoginToReturn(NETWORK_ERROR_RESULT)
        val states = useCase.login(USERNAME, PASSWORD).toList()
        assertThat(states).isEqualTo(listOf(LOADING_STATE, NETWORK_ERROR_STATE))
    }

    @Test
    fun testInvalidUsername() = runBlockingTest {
        val states = useCase.login("", PASSWORD).toList()
        assertThat(states).isEqualTo(listOf(INVALID_USERNAME_STATE))
    }

    @Test
    fun testInvalidPassword() = runBlockingTest {
        val states = useCase.login(USERNAME, "").toList()
        assertThat(states).isEqualTo(listOf(INVALID_PASSWORD_STATE))
    }

    private suspend fun stubLoginToReturn(result: AsyncResult<AuthSession>) {
        coEvery { repository.login(USERNAME, PASSWORD) } returns flowOf(LOADING_STATE, result)
    }
}
