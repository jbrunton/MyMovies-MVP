package com.jbrunton.mymovies.main

import com.jbrunton.mymovies.nav.Navigator
import com.jbrunton.mymovies.shared.BaseViewModel

class MainViewModel(val navigator: Navigator) : BaseViewModel() {
    override fun start() {
        showSearch()
    }

    fun showSearch() {
        navigator.showSearch()
    }

    fun showDiscover() {
        navigator.showDiscover()
    }

    fun showAccount() {
        navigator.showAccount()
    }
}