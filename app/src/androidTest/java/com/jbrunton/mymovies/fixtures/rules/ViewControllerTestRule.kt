package com.jbrunton.mymovies.fixtures.rules

import androidx.appcompat.app.AppCompatActivity
import com.jbrunton.mymovies.ui.shared.ViewController

class ViewControllerTestRule<T>(
        val viewController: ViewController<T>,
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
        return ViewControllerTestFragment(viewController)
    }

    public fun setViewState(viewState: T) {
        runOnUiThread { viewController.updateView(viewState) }
    }

    companion object {
        fun <T> create(
                viewController: ViewController<T>,
                initialTouchMode: Boolean = false,
                launchActivity: Boolean = true,
                launchFragment: Boolean = true
        ): ViewControllerTestRule<T> = ViewControllerTestRule(
                viewController,
                initialTouchMode,
                launchActivity,
                launchFragment
        )
    }
}