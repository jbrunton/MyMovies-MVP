package com.jbrunton.mymovies.nav

import android.content.Intent
import androidx.fragment.app.FragmentActivity
import com.jbrunton.entities.models.Genre
import com.jbrunton.mymovies.R
import com.jbrunton.mymovies.ui.account.AccountFragment
import com.jbrunton.mymovies.ui.account.favorites.FavoritesActivity
import com.jbrunton.mymovies.ui.auth.LoginActivity
import com.jbrunton.mymovies.ui.discover.DiscoverFragment
import com.jbrunton.mymovies.ui.discover.genres.GenreResultsActivity
import com.jbrunton.mymovies.ui.main.MainActivity
import com.jbrunton.mymovies.ui.search.SearchFragment
import com.jbrunton.mymovies.usecases.nav.NavigationRequest
import com.jbrunton.mymovies.usecases.nav.NavigationResult

interface NavigationContext {
    fun onNavigationResult(result: NavigationResult)
}

class Navigator(val activity: FragmentActivity, val router: ResultRouter) {
    fun navigate(request: NavigationRequest) = when (request) {
        is NavigationRequest.SearchRequest -> showSearch()
        is NavigationRequest.DiscoverRequest -> showDiscover()
        is NavigationRequest.AccountRequest -> showAccount()

        is NavigationRequest.GenreRequest -> startGenreActivity(request.genre)
        is NavigationRequest.FavoritesRequest -> startFavoritesActivity()
        is NavigationRequest.LoginRequest -> startLoginActivity()
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?, context: NavigationContext) {
        when (requestCode) {
            LoginActivity.LOGIN_REQUEST -> {
                if (resultCode == LoginActivity.LOGIN_SUCCESSFUL) {
                    val authSession = LoginActivity.fromIntent(data!!)
                    val result = NavigationResult.LoginSuccess(authSession)
                    context.onNavigationResult(result)
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

    private fun startGenreActivity(genre: Genre) {
        val intent = Intent(activity, GenreResultsActivity::class.java)
        intent.putExtra("GENRE_ID", genre.id)
        intent.putExtra("GENRE_NAME", genre.name)
        activity.startActivity(intent)
    }

    private fun startLoginActivity() {
        val intent = Intent(activity, LoginActivity::class.java)
        activity.startActivityForResult(intent, LoginActivity.LOGIN_REQUEST)
    }

//    fun login(): Observable<AuthSession> {
//        val observable: PublishSubject<AuthSession> = PublishSubject.create()
//        router.route(LoginActivity.LOGIN_REQUEST, { resultCode, data ->
//            if (data != null) {
//                observable.onNext(LoginActivity.fromIntent(data))
//            } else {
//                observable.onComplete()
//            }
//        })
//        val intent = Intent(activity, LoginActivity::class.java)
//        activity.startActivityForResult(intent, LoginActivity.LOGIN_REQUEST)
//        return observable
//    }

    private fun startFavoritesActivity() {
        val intent = Intent(activity, FavoritesActivity::class.java)
        activity.startActivity(intent)
    }
}