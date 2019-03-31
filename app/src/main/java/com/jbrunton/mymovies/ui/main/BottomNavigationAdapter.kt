package com.jbrunton.mymovies.ui.main

import android.view.MenuItem
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.jbrunton.mymovies.R
import com.jbrunton.mymovies.usecases.nav.NavigationRequest
import com.jbrunton.mymovies.nav.NavigationController

class BottomNavigationAdapter(
        val navigationController: NavigationController
) : BottomNavigationView.OnNavigationItemSelectedListener{

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.navigation_search -> {
                navigationController.navigate(NavigationRequest.SearchRequest)
                return true
            }
            R.id.navigation_discover -> {
                navigationController.navigate(NavigationRequest.DiscoverRequest)
                return true
            }
            R.id.navigation_account -> {
                navigationController.navigate(NavigationRequest.AccountRequest)
                return true
            }
        }
        return false
    }
}