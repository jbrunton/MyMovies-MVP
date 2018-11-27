package com.jbrunton.mymovies.ui.main

import android.view.MenuItem
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.jbrunton.mymovies.R
import com.jbrunton.mymovies.nav.Navigator

class BottomNavigationAdapter(
        val navigator: Navigator
) : BottomNavigationView.OnNavigationItemSelectedListener{

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.navigation_search -> {
                navigator.showSearch()
                return true
            }
            R.id.navigation_discover -> {
                navigator.showDiscover()
                return true
            }
            R.id.navigation_account -> {
                navigator.showAccount()
                return true
            }
        }
        return false
    }
}