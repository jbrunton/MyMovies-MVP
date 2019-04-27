package com.jbrunton.mymovies.libs.ui.nav

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import java.util.*

interface NavigationRequest

interface NavigationResult

interface NavigationRequestListener {
    fun onNavigationRequest(request: NavigationRequest)
}

interface NavigationResultListener {
    fun onNavigationResult(result: NavigationResult)
}

interface NavigationController {
    fun navigate(request: NavigationRequest)
    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
}

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
