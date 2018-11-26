package com.jbrunton.mymovies.nav

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.jbrunton.entities.models.AuthSession
import com.jbrunton.mymovies.MyMoviesApplication
import com.jbrunton.mymovies.main.MainActivity
import com.jbrunton.mymovies.R
import com.jbrunton.mymovies.account.AccountFragment
import com.jbrunton.mymovies.auth.LoginActivity
import com.jbrunton.mymovies.discover.DiscoverFragment
import com.jbrunton.mymovies.search.SearchFragment
import com.jbrunton.mymovies.shared.BaseFragment
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import org.koin.standalone.KoinComponent
import org.koin.standalone.get
import org.koin.standalone.inject
import java.util.*

class Navigator : KoinComponent {
    private val resultHandlers = LinkedList<ResultHandler>()

    abstract class ResultHandler(val requestCode: Int) {
        fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?): Boolean {
            if (requestCode == this.requestCode) {
                onResult(resultCode, data)
                return true
            }
            return false
        }

        abstract fun onResult(resultCode: Int, data: Intent?)
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val it = resultHandlers.iterator()
        while (it.hasNext()) {
            val handler = it.next()
            if (handler.onActivityResult(requestCode, resultCode, data)) {
                it.remove()
            }
        }
    }

    fun showSearch() {
        mainActivity.supportFragmentManager.beginTransaction()
                .replace(R.id.content, SearchFragment())
                .commit()
    }

    fun showDiscover() {
        mainActivity.supportFragmentManager.beginTransaction()
                .replace(R.id.content, DiscoverFragment())
                .commit()
    }

    fun showAccount() {
        mainActivity.supportFragmentManager.beginTransaction()
                .replace(R.id.content, AccountFragment())
                .commit()
    }

    fun login(): Observable<AuthSession> {
        val observable: PublishSubject<AuthSession> = PublishSubject.create()
        val intent = Intent(activity, LoginActivity::class.java)
        resultHandlers.add(object : ResultHandler(LoginActivity.LOGIN_REQUEST){
            override fun onResult(resultCode: Int, data: Intent?) {
                observable.onNext(AuthSession(""))
            }

        })
        activity.startActivityForResult(intent, LoginActivity.LOGIN_REQUEST)
        return observable
    }

    private val activity: AppCompatActivity
        get() = get()

    private val mainActivity: MainActivity
        get() = activity as MainActivity
}