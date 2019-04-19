package com.jbrunton.libs.ui

interface ViewModelLifecycle {
    fun onBindListeners()
    fun onObserveData()
    fun onCreateLayout()
}
