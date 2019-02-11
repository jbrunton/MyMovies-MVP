package com.jbrunton.mymovies.fixtures

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.jbrunton.mymovies.ui.shared.LayoutManager

abstract class LayoutTestFragment<T>: Fragment() {
    lateinit var layoutManager: LayoutManager<T>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(layoutManager.layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        layoutManager = createLayoutManager(view)
    }

    fun updateView(viewState: T) {
        layoutManager.updateView(viewState)
    }

    protected abstract fun createLayoutManager(view: View): LayoutManager<T>
}