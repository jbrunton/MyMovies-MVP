package com.jbrunton.mymovies.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.crashlytics.android.Crashlytics
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.jbrunton.mymovies.R
import com.jbrunton.mymovies.nav.Navigator
import com.jbrunton.mymovies.search.SearchFragment
import com.jbrunton.mymovies.shared.BaseActivity
import io.fabric.sdk.android.Fabric
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.inject

class MainActivity : BaseActivity<MainViewModel>() {

    lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = get()

        Fabric.with(this, Crashlytics())
        setContentView(R.layout.activity_main)

        navigation.setOnNavigationItemSelectedListener(BottomNavigationAdapter(viewModel))

        if (savedInstanceState == null) {
            viewModel.start()
        }
    }
}
