package com.jbrunton.mymovies.fixtures

import android.view.View
import android.widget.EditText
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers
import org.hamcrest.Matcher



object EditTextViewActions {
    fun hideCursor(): ViewAction {
        return ViewActions.actionWithAssertions(object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                return ViewMatchers.isAssignableFrom(EditText::class.java)
            }

            override fun getDescription(): String {
                return "replace the EditText drawable"
            }

            override fun perform(uiController: UiController, view: View) {
                // Replace the indeterminate drawable with a static red ColorDrawable
                val editText = view as EditText
                editText.isCursorVisible = false
                uiController.loopMainThreadUntilIdle()
            }
        })
    }
}