package com.jbrunton.libs.ui

import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import kotlinx.android.extensions.LayoutContainer

abstract class ViewController<T>: LayoutContainer {
    abstract @get:LayoutRes val layout: Int

    override lateinit var containerView: View
    val context get() = containerView.context

    open fun bind(containerView: View) {
        this.containerView = containerView
    }

    fun bind(activity: AppCompatActivity) {
        bind(activity.findViewById<View>(android.R.id.content))
    }

    fun bind(fragment: Fragment) {
        bind(fragment.view!!)
    }

    abstract fun updateView(viewState: T)
}
