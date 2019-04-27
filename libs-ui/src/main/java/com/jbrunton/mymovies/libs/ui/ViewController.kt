package com.jbrunton.mymovies.libs.ui

import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

abstract class ViewController<T> {
    abstract @get:LayoutRes val layout: Int

    lateinit var containerView: View
    val context get() = containerView.context

    open fun initializeView(containerView: View) {
        this.containerView = containerView
    }

    fun initializeView(activity: AppCompatActivity) {
        initializeView(activity.findViewById<View>(android.R.id.content))
    }

    fun initializeView(fragment: Fragment) {
        initializeView(fragment.view!!)
    }

    abstract fun updateView(viewState: T)
}
