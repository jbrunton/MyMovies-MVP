package com.jbrunton.mymovies.fixtures

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.jbrunton.mymovies.ui.shared.ViewController

abstract class ViewControllerTestFragment<T>: Fragment() {
    lateinit var viewController: ViewController<T>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewController = createViewController()
        return inflater.inflate(viewController.layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewController.bind(view)
    }

    fun updateView(viewState: T) {
        viewController.updateView(viewState)
    }

    protected abstract fun createViewController(): ViewController<T>
}