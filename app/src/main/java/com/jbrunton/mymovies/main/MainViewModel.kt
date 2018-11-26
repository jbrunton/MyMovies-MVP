package com.jbrunton.mymovies.main

import com.jbrunton.mymovies.nav.Navigator
import com.jbrunton.mymovies.shared.BaseViewModel

class MainViewModel(val navigator: Navigator) : BaseViewModel() {
    override fun start() {}

    fun showSearch(activity: MainActivity) {
        navigator.search()
    }

    fun showDiscover(activity: MainActivity) {
        navigator.discover()
    }

    fun showAccount(activity: MainActivity) {
        navigator.account()
    }
}