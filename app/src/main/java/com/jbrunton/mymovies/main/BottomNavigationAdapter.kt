package com.jbrunton.mymovies.main

import android.view.MenuItem
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.jbrunton.mymovies.R

class BottomNavigationAdapter(
        val viewModel: MainViewModel
) : BottomNavigationView.OnNavigationItemSelectedListener{

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.navigation_search -> {
                viewModel.showSearch()
                return true
            }
            R.id.navigation_discover -> {
                viewModel.showDiscover()
                return true
            }
            R.id.navigation_account -> {
                viewModel.showAccount()
                return true
            }
        }
        return false
    }
}