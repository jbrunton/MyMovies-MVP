package com.jbrunton.mymovies.nav

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.jbrunton.mymovies.main.MainActivity
import com.jbrunton.mymovies.R
import com.jbrunton.mymovies.account.AccountFragment
import com.jbrunton.mymovies.auth.LoginActivity
import com.jbrunton.mymovies.discover.DiscoverFragment
import com.jbrunton.mymovies.search.SearchFragment
import com.jbrunton.mymovies.shared.BaseFragment
import org.koin.standalone.KoinComponent
import org.koin.standalone.get
import org.koin.standalone.inject

class Navigator : KoinComponent {
    fun search() {
        activity.supportFragmentManager.beginTransaction()
                .replace(R.id.content, SearchFragment())
                .commit()
    }

    fun discover() {
        activity.supportFragmentManager.beginTransaction()
                .replace(R.id.content, DiscoverFragment())
                .commit()
    }

    fun account() {
        activity.supportFragmentManager.beginTransaction()
                .replace(R.id.content, AccountFragment())
                .commit()
    }

    fun login() {
        val intent = Intent(activity, LoginActivity::class.java)
        activity.startActivityForResult(intent, LoginActivity.LOGIN_REQUEST)
    }

    private val activity: AppCompatActivity
        get() = get()
}