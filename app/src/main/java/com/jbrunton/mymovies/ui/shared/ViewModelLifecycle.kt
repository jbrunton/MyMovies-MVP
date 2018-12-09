package com.jbrunton.mymovies.ui.shared

interface ViewModelLifecycle {
    fun onBindListeners()
    fun onObserveData()
    fun onCreateLayout()
}
