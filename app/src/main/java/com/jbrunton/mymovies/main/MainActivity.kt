package com.jbrunton.mymovies.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.crashlytics.android.Crashlytics
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.jbrunton.mymovies.R
import com.jbrunton.mymovies.nav.Navigator
import com.jbrunton.mymovies.search.SearchFragment
import io.fabric.sdk.android.Fabric
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {

    val viewModel: MainViewModel by inject()

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_search -> {
                viewModel.showSearch(this)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_discover -> {
                viewModel.showDiscover(this)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_account -> {
                viewModel.showAccount(this)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Fabric.with(this, Crashlytics())
        setContentView(R.layout.activity_main)

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.content, SearchFragment())
                    .commit()
        }
    }
}
