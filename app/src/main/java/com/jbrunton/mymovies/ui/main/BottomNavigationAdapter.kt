package com.jbrunton.mymovies.ui.main

import android.view.MenuItem
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.jbrunton.mymovies.libs.ui.NavigationController
import com.jbrunton.mymovies.R
import com.jbrunton.mymovies.libs.ui.AccountRequest
import com.jbrunton.mymovies.libs.ui.DiscoverRequest
import com.jbrunton.mymovies.libs.ui.SearchRequest

class BottomNavigationAdapter(
        val navigationController: NavigationController
) : BottomNavigationView.OnNavigationItemSelectedListener{

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.navigation_search -> {
                navigationController.navigate(SearchRequest)
                return true
            }
            R.id.navigation_discover -> {
                navigationController.navigate(DiscoverRequest)
                return true
            }
            R.id.navigation_account -> {
                navigationController.navigate(AccountRequest)
                return true
            }
        }
        return false
    }
}