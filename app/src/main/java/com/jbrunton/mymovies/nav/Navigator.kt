package com.jbrunton.mymovies.nav

import android.app.Activity
import android.content.Intent
import com.jbrunton.mymovies.main.MainActivity
import com.jbrunton.mymovies.R
import com.jbrunton.mymovies.account.AccountFragment
import com.jbrunton.mymovies.auth.LoginActivity
import com.jbrunton.mymovies.discover.DiscoverFragment
import com.jbrunton.mymovies.search.SearchFragment
import com.jbrunton.mymovies.shared.BaseFragment

class Navigator(val activity: Activity) {
    fun search(activity: MainActivity) {
        activity.supportFragmentManager.beginTransaction()
                .replace(R.id.content, SearchFragment())
                .commit()
    }

    fun discover(activity: MainActivity) {
        activity.supportFragmentManager.beginTransaction()
                .replace(R.id.content, DiscoverFragment())
                .commit()
    }

    fun account(activity: MainActivity) {
        activity.supportFragmentManager.beginTransaction()
                .replace(R.id.content, AccountFragment())
                .commit()
    }

    fun login(fragment: BaseFragment<*>) {
        val intent = Intent(fragment.context, LoginActivity::class.java)
        fragment.startActivityForResult(intent, LoginActivity.LOGIN_REQUEST)
    }
}