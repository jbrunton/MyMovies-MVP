package com.jbrunton.mymovies.ui.main

import com.jbrunton.mymovies.entities.SchedulerFactory
import com.jbrunton.mymovies.libs.ui.nav.Navigator
import com.jbrunton.mymovies.libs.ui.viewmodels.BaseViewModel
import org.koin.core.Koin

class MainViewModel(
        navigator: Navigator,
        schedulerFactory: SchedulerFactory
) : BaseViewModel(navigator, schedulerFactory) {
    override fun start() {}
}
