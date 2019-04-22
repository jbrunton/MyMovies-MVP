package com.jbrunton.mymovies.libs.ui

interface ViewModelLifecycle {
    fun onBindListeners()
    fun onObserveData()
    fun onCreateLayout()
}
