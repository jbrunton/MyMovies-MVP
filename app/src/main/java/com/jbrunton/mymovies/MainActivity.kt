package com.jbrunton.mymovies

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import com.crashlytics.android.Crashlytics
import com.jbrunton.mymovies.discover.DiscoverFragment
import com.jbrunton.mymovies.search.SearchFragment
import io.fabric.sdk.android.Fabric
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_search -> {
                supportFragmentManager.beginTransaction()
                        .replace(R.id.content, SearchFragment())
                        .commit()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_discover -> {
                supportFragmentManager.beginTransaction()
                        .replace(R.id.content, DiscoverFragment())
                        .commit()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> return@OnNavigationItemSelectedListener true
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
