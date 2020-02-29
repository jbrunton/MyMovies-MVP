package com.jbrunton.mymovies.features.discover

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.jbrunton.async.AsyncResult
import com.jbrunton.async.AsyncResult.Companion.loading
import com.jbrunton.async.AsyncResult.Companion.success
import com.jbrunton.inject.Container
import com.jbrunton.inject.module
import com.jbrunton.mymovies.entities.SchedulerFactory
import com.jbrunton.mymovies.features.discover.DiscoverViewStateFactory.viewState
import com.jbrunton.mymovies.fixtures.ImmediateSchedulerFactory
import com.jbrunton.mymovies.fixtures.MainCoroutineScopeRule
import com.jbrunton.mymovies.libs.ui.nav.Navigator
import com.jbrunton.mymovies.libs.ui.viewstates.LoadingViewState
import com.jbrunton.mymovies.usecases.discover.DiscoverState
import com.jbrunton.mymovies.usecases.discover.DiscoverUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.flow.flowOf
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class DiscoverViewModelTest {
    @get:Rule val rule: TestRule = InstantTaskExecutorRule()
    @get:Rule val coroutineScope =  MainCoroutineScopeRule()

    private lateinit var viewModel: DiscoverViewModel
    private lateinit var container: Container
    @RelaxedMockK lateinit var navigator: Navigator
    @MockK lateinit var useCase: DiscoverUseCase

    private lateinit var viewStates: MutableList<LoadingViewState<DiscoverViewState>>

    private val schedulerFactory: SchedulerFactory = ImmediateSchedulerFactory()

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        container = createContainer()
        viewModel = DiscoverViewModel(container)
        viewStates = mutableListOf()
        viewModel.viewState.observeForever(viewStateObserver)
    }

    @After
    fun tearDown() {
        viewModel.viewState.removeObserver(viewStateObserver)
    }

    @Test
    fun initializesToLoadingState() {
        assertThat(viewModel.viewState.getOrAwaitValue()).isEqualTo(LoadingViewState.loading(DiscoverViewState.Empty))
    }

    @Test
    fun loadsMovies() {
        val discoverState = DiscoverState(emptyList(), emptyList(), emptyList())
        coEvery { useCase.discover() } returns flowOf(AsyncResult.loading(null), success(discoverState))

        viewModel.start()

        assertThat(viewStates).isEqualTo(
                listOf(
                        viewState(loading(null)),
                        viewState(success(discoverState))
                ))
    }

    private val viewStateObserver = Observer<LoadingViewState<DiscoverViewState>> {
        viewStates.add(it)
    }

    private fun createContainer(): Container {
        val module = module {
            single { schedulerFactory }
            single { navigator }
            single { useCase }
        }
        return Container().apply {
            register(module)
        }
    }
}
