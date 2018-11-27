package com.jbrunton.mymovies.ui.main

import android.os.Bundle
import com.crashlytics.android.Crashlytics
import com.jbrunton.mymovies.R
import com.jbrunton.mymovies.ui.shared.BaseActivity
import com.jbrunton.mymovies.ui.shared.BaseViewModel
import io.fabric.sdk.android.Fabric
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity<MainActivity.MainViewModel>() {

    class MainViewModel : BaseViewModel() {
        override fun start() {}
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Fabric.with(this, Crashlytics())
        setContentView(R.layout.activity_main)

        navigation.setOnNavigationItemSelectedListener(BottomNavigationAdapter(navigator))

        if (savedInstanceState == null) {
            navigator.showSearch()
        }
    }
}
