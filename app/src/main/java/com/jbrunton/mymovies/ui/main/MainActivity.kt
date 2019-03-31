package com.jbrunton.mymovies.ui.main

import android.os.Bundle
import com.crashlytics.android.Crashlytics
import com.jbrunton.inject.injectViewModel
import com.jbrunton.mymovies.R
import com.jbrunton.mymovies.nav.NavigationRequest
import com.jbrunton.mymovies.ui.shared.BaseActivity
import io.fabric.sdk.android.Fabric
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity<MainViewModel>() {
    override val viewModel: MainViewModel by injectViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Fabric.with(this, Crashlytics())

        if (savedInstanceState == null) {
            navigator.navigate(NavigationRequest.SearchRequest)
        }
    }

    override fun onCreateLayout() {
        setContentView(R.layout.activity_main)
    }

    override fun onBindListeners() {
        navigation.setOnNavigationItemSelectedListener(BottomNavigationAdapter(navigator))
    }
}
