package com.jbrunton.mymovies.features.discover

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.jbrunton.inject.Container
import com.jbrunton.inject.module
import com.jbrunton.mymovies.entities.SchedulerFactory
import com.jbrunton.mymovies.fixtures.ImmediateSchedulerFactory
import com.jbrunton.mymovies.libs.ui.nav.Navigator
import com.jbrunton.mymovies.libs.ui.viewstates.LoadingViewState
import com.jbrunton.mymovies.usecases.discover.DiscoverUseCase
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class DiscoverViewModelTest {
    @get:Rule
    val rule: TestRule = InstantTaskExecutorRule()

    private lateinit var viewModel: DiscoverViewModel
    private lateinit var container: Container
    @MockK lateinit var navigator: Navigator
    @MockK lateinit var useCase: DiscoverUseCase

    private val schedulerFactory: SchedulerFactory = ImmediateSchedulerFactory()

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        container = createContainer()
        viewModel = DiscoverViewModel(container)
    }

    @Test
    fun initializesToLoadingState() {
        assertThat(viewModel.viewState.value).isEqualTo(LoadingViewState.loading(DiscoverViewState.Empty))
    }

    private fun createContainer(): Container {
        val module = module {
            single { schedulerFactory }
            single { navigator }
        }
        return Container().apply {
            register(module)
        }
    }
}
