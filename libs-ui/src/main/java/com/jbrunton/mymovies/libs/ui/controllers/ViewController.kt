package com.jbrunton.mymovies.libs.ui.controllers

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

abstract class ViewController<T> {
    lateinit var view: View
    val context get() = view.context

    open fun onViewCreated(containerView: View) {
        this.view = containerView
    }

    abstract fun updateView(viewState: T)
}

val AppCompatActivity.rootView: View get() = findViewById<View>(android.R.id.content)

val Fragment.rootView get() = view!!
