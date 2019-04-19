package com.jbrunton.mymovies.nav

import android.content.Intent
import androidx.fragment.app.FragmentActivity
import com.jbrunton.libs.ui.NavigationRequest
import com.jbrunton.libs.ui.Navigator
import com.jbrunton.mymovies.R
import com.jbrunton.mymovies.ui.account.AccountFragment
import com.jbrunton.mymovies.ui.account.favorites.FavoritesActivity
import com.jbrunton.mymovies.ui.auth.LoginActivity
import com.jbrunton.mymovies.ui.discover.DiscoverFragment
import com.jbrunton.mymovies.ui.main.MainActivity
import com.jbrunton.mymovies.ui.search.SearchFragment

class NavigationController(val activity: FragmentActivity, val navigator: Navigator) {
    fun navigate(request: NavigationRequest) = when (request) {
        is SearchRequest -> showSearch()
        is DiscoverRequest -> showDiscover()
        is AccountRequest -> showAccount()

        is FavoritesRequest -> startFavoritesActivity()
        is LoginRequest -> startLoginActivity()
        else -> throw IllegalArgumentException("Unexpected navigation request: ${request}")
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            LoginActivity.LOGIN_REQUEST -> {
                if (resultCode == LoginActivity.LOGIN_SUCCESSFUL) {
                    val authSession = LoginActivity.fromIntent(data!!)
                    val result = LoginSuccess(authSession)
                    navigator.onNavigationResult(result)
                }
            }
            else -> throw IllegalStateException("Unexpected requestCode: ${requestCode}")
        }
    }

    private fun showSearch() {
        (activity as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.content, SearchFragment())
                .commit()
    }

    private fun showDiscover() {
        (activity as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.content, DiscoverFragment())
                .commit()
    }

    private fun showAccount() {
        (activity as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.content, AccountFragment())
                .commit()
    }

    private fun startLoginActivity() {
        val intent = Intent(activity, LoginActivity::class.java)
        activity.startActivityForResult(intent, LoginActivity.LOGIN_REQUEST)
    }

    private fun startFavoritesActivity() {
        val intent = Intent(activity, FavoritesActivity::class.java)
        activity.startActivity(intent)
    }
}