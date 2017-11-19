package com.jbrunton.mymovies.fixtures;

import android.app.Activity;

import com.google.android.libraries.cloudtesting.screenshots.ScreenShotter;
import com.squareup.spoon.Spoon;

public class ScreenshotHelper {
    public static void takeScreenshot(Activity activity) {
        takeScreenshot(activity, "_");
    }

    public static void takeScreenshot(Activity activity, String tag) {
        Spoon.screenshot(activity, tag);
        ScreenShotter.takeScreenshot(tag, activity);
    }
}
