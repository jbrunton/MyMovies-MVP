package com.jbrunton.mymovies.nav

import android.content.Intent
import androidx.fragment.app.FragmentActivity
import com.jbrunton.mymovies.R
import com.jbrunton.mymovies.features.account.AccountFragment
import com.jbrunton.mymovies.features.account.favorites.FavoritesActivity
import com.jbrunton.mymovies.features.search.SearchFragment
import com.jbrunton.mymovies.ui.auth.LoginActivity
import com.jbrunton.mymovies.features.discover.DiscoverFragment
import com.jbrunton.mymovies.libs.ui.nav.*
import com.jbrunton.mymovies.ui.main.MainActivity
import com.jbrunton.mymovies.ui.moviedetails.MovieDetailsActivity

class AppNavigationController(val activity: FragmentActivity, val navigator: Navigator): NavigationController {
    override fun navigate(request: NavigationRequest) = when (request) {
        is SearchRequest -> showSearch()
        is DiscoverRequest -> showDiscover()
        is AccountRequest -> showAccount()

        is FavoritesRequest -> startFavoritesActivity()
        is LoginRequest -> startLoginActivity()
        is MovieDetailsRequest -> showMovieDetailsActivity(request)
        else -> throw IllegalArgumentException("Unexpected navigation request: ${request}")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
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

    private fun showMovieDetailsActivity(request: MovieDetailsRequest) {
        val intent = Intent(activity, MovieDetailsActivity::class.java)
        intent.putExtra("MOVIE_ID", request.movieId)
        activity.startActivity(intent)
    }
}