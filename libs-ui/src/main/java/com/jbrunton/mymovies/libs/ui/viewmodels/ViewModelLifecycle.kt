package com.jbrunton.mymovies.libs.ui.viewmodels

interface ViewModelLifecycle {
    fun onBindListeners()
    fun onObserveData()
    fun onCreateLayout()
}
