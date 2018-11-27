package com.jbrunton.mymovies.di

import androidx.fragment.app.FragmentActivity
import com.jbrunton.mymovies.nav.Navigator

class ActivityModule(val activity: FragmentActivity): Module {
    override fun registerTypes(container: Container) {
        container.apply {
            single { activity }
            single { Navigator(get()) }
        }
    }
}
