package com.jbrunton.mymovies.fixtures

import android.graphics.drawable.ColorDrawable
import android.support.test.espresso.UiController
import android.support.test.espresso.ViewAction
import android.support.test.espresso.action.ViewActions.actionWithAssertions
import android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom
import android.view.View
import android.widget.ProgressBar
import org.hamcrest.Matcher

object ProgressBarViewActions {
    fun replaceProgressBarDrawable(): ViewAction {
        return actionWithAssertions(object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                return isAssignableFrom(ProgressBar::class.java)
            }

            override fun getDescription(): String {
                return "replace the ProgressBar drawable"
            }

            override fun perform(uiController: UiController, view: View) {
                // Replace the indeterminate drawable with a static red ColorDrawable
                val progressBar = view as ProgressBar
                progressBar.indeterminateDrawable = ColorDrawable(-0x10000)
                uiController.loopMainThreadUntilIdle()
            }
        })
    }
}
