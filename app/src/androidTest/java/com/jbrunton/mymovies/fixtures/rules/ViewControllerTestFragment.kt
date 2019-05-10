package com.jbrunton.mymovies.fixtures.rules

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import com.jbrunton.mymovies.libs.ui.controllers.ViewController

class ViewControllerTestFragment<T>(@get:LayoutRes val layoutId: Int, val viewController: ViewController<T>): Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(layoutId, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewController.onViewCreated(view)
    }
}