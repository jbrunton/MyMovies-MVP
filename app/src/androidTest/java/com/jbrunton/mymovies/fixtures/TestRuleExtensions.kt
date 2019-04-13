package com.jbrunton.mymovies.fixtures

import android.app.Activity
import androidx.test.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.google.android.libraries.cloudtesting.screenshots.ScreenShotter
import com.jbrunton.inject.HasContainer
import com.jbrunton.mymovies.MyMoviesApplication

fun <T : Activity> ActivityTestRule<T>.takeScreenshot(tag: String = "_") {
    ScreenShotter.takeScreenshot(tag, activity)
}

val <T : Activity> ActivityTestRule<T>.application get() =
    InstrumentationRegistry.getInstrumentation().getTargetContext().getApplicationContext() as MyMoviesApplication

val <T : Activity> ActivityTestRule<T>.container get() =
    (activity as? HasContainer)?.container ?: (application).container

