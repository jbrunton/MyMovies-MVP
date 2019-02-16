package com.jbrunton.mymovies.nav

import android.content.Intent
import androidx.fragment.app.FragmentActivity
import com.jbrunton.entities.models.AuthSession
import com.jbrunton.entities.models.Genre
import com.jbrunton.mymovies.R
import com.jbrunton.mymovies.ui.account.AccountFragment
import com.jbrunton.mymovies.ui.account.favorites.FavoritesActivity
import com.jbrunton.mymovies.ui.auth.LoginActivity
import com.jbrunton.mymovies.ui.discover.DiscoverFragment
import com.jbrunton.mymovies.ui.discover.GenreResultsActivity
import com.jbrunton.mymovies.ui.main.MainActivity
import com.jbrunton.mymovies.ui.search.SearchFragment
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

class Navigator(val activity: FragmentActivity, val router: ResultRouter) {
    fun showSearch() {
        (activity as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.movie_details, SearchFragment())
                .commit()
    }

    fun showDiscover() {
        (activity as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.movie_details, DiscoverFragment())
                .commit()
    }

    fun showAccount() {
        (activity as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.movie_details, AccountFragment())
                .commit()
    }

    fun startGenreActivity(genre: Genre) {
        val intent = Intent(activity, GenreResultsActivity::class.java)
        intent.putExtra("GENRE_ID", genre.id)
        intent.putExtra("GENRE_NAME", genre.name)
        activity.startActivity(intent)
    }

    fun login(): Observable<AuthSession?> {
        val observable: PublishSubject<AuthSession> = PublishSubject.create()
        router.route(LoginActivity.LOGIN_REQUEST, { resultCode, data ->
            if (data != null) {
                observable.onNext(LoginActivity.fromIntent(data))
            } else {
                observable.onComplete()
            }
        })
        val intent = Intent(activity, LoginActivity::class.java)
        activity.startActivityForResult(intent, LoginActivity.LOGIN_REQUEST)
        return observable
    }

    fun showFavorites() {
        val intent = Intent(activity, FavoritesActivity::class.java)
        activity.startActivity(intent)
    }
}