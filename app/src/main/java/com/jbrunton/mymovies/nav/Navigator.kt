package com.jbrunton.mymovies.nav

import com.jbrunton.mymovies.usecases.nav.NavigationRequest
import com.jbrunton.mymovies.usecases.nav.NavigationRequestListener
import com.jbrunton.mymovies.usecases.nav.NavigationResult
import com.jbrunton.mymovies.usecases.nav.NavigationResultListener
import java.lang.ref.WeakReference
import java.util.*

class Navigator {
    private val requestListeners = LinkedList<NavigationRequestListener>()
    private val resultListeners = LinkedList<WeakReference<NavigationResultListener>>()

    fun register(listener: NavigationRequestListener) {
        requestListeners.add(listener)
    }

    fun unregister(listener: NavigationRequestListener) {
        requestListeners.remove(listener)
    }

    fun register(listener: NavigationResultListener) {
        resultListeners.add(WeakReference(listener))
    }

    fun navigate(request: NavigationRequest) {
        requestListeners.forEach {
            it.onNavigationRequest(request)
        }
    }

    fun onNavigationResult(result: NavigationResult) {
        resultListeners.forEach {
            it.get()?.onNavigationResult(result)
        }
    }
}
