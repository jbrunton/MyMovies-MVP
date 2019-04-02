package com.jbrunton.mymovies.nav

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import com.jbrunton.mymovies.usecases.nav.NavigationRequest
import com.jbrunton.mymovies.usecases.nav.NavigationRequestListener
import com.jbrunton.mymovies.usecases.nav.NavigationResult
import com.jbrunton.mymovies.usecases.nav.NavigationResultListener
import java.util.*

class Navigator {
    private val requestListeners = LinkedList<NavigationRequestListener>()
    private val resultListeners = LinkedList<NavigationResultListener>()

    fun <T> register(listener: T)
            where T : NavigationRequestListener,
                  T : AppCompatActivity
    {
        requestListeners.add(listener)
    }

    fun <T> unregister(listener: T)
            where T : NavigationRequestListener,
                  T : AppCompatActivity
    {
        requestListeners.remove(listener)
    }

    fun <T> register(listener: T)
            where T : NavigationResultListener,
                  T : ViewModel
    {
        resultListeners.add(listener)
    }

    fun <T> unregister(listener: T)
            where T : NavigationResultListener,
                  T : ViewModel
    {
        resultListeners.remove(listener)
    }

    fun navigate(request: NavigationRequest) {
        requestListeners.forEach {
            it.onNavigationRequest(request)
        }
    }

    fun onNavigationResult(result: NavigationResult) {
        resultListeners.forEach {
            it.onNavigationResult(result)
        }
    }
}
