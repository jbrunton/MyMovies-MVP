package com.jbrunton.mymovies.fixtures.rules

import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import com.jbrunton.mymovies.libs.ui.controllers.ViewController

class ViewControllerTestRule<T>(
        val viewController: ViewController<T>,
        @get:LayoutRes val layoutId: Int,
        initialTouchMode: Boolean,
        launchActivity: Boolean,
        launchFragment: Boolean
): FragmentTestRule<AppCompatActivity, ViewControllerTestFragment<*>>(
        AppCompatActivity::class.java,
        ViewControllerTestFragment::class.java,
        initialTouchMode = initialTouchMode,
        launchActivity = launchActivity,
        launchFragment = launchFragment
) {

    override fun createFragment(): ViewControllerTestFragment<T> {
        return ViewControllerTestFragment(viewController, layoutId)
    }

    fun setViewState(viewState: T) {
        runOnUiThread { viewController.updateView(viewState) }
    }

    companion object {
        fun <T> create(
                viewController: ViewController<T>,
                @LayoutRes layoutId: Int,
                initialTouchMode: Boolean = false,
                launchActivity: Boolean = true,
                launchFragment: Boolean = true
        ): ViewControllerTestRule<T> = ViewControllerTestRule(
                viewController,
                layoutId,
                initialTouchMode,
                launchActivity,
                launchFragment
        )
    }
}