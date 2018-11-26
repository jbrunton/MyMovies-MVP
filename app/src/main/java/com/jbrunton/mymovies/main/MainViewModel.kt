package com.jbrunton.mymovies.main

import com.jbrunton.mymovies.nav.Navigator
import com.jbrunton.mymovies.shared.BaseViewModel

class MainViewModel(val navigator: Navigator) : BaseViewModel() {
    override fun start() {}

    fun showSearch(activity: MainActivity) {
        navigator.showSearch()
    }

    fun showDiscover(activity: MainActivity) {
        navigator.showDiscover()
    }

    fun showAccount(activity: MainActivity) {
        navigator.showAccount()
    }
}