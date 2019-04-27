package com.jbrunton.mymovies.libs.ui.controllers

import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

abstract class ViewController<T> {
    abstract @get:LayoutRes val layout: Int

    lateinit var view: View
    val context get() = view.context

    open fun initializeView(containerView: View) {
        this.view = containerView
    }

    fun initializeView(activity: AppCompatActivity) {
        initializeView(activity.findViewById<View>(android.R.id.content))
    }

    fun initializeView(fragment: Fragment) {
        initializeView(fragment.view!!)
    }

    abstract fun updateView(viewState: T)
}
