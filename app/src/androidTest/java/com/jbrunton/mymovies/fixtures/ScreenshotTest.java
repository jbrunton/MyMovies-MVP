package com.jbrunton.mymovies.fixtures;

import android.support.test.rule.ActivityTestRule;

import com.google.android.libraries.cloudtesting.screenshots.ScreenShotter;
import com.jbrunton.mymovies.app.MainActivity;
import com.squareup.spoon.SpoonRule;

import org.junit.Rule;

public class ScreenshotTest {
    @Rule public ActivityTestRule<MainActivity> activityRule =
            new ActivityTestRule<>(MainActivity.class);
    @Rule public SpoonRule spoonRule = new SpoonRule();

    public void takeScreenshot() {
        takeScreenshot("_");
    }

    public void takeScreenshot(String tag) {
        spoonRule.screenshot(activityRule.getActivity(), tag);
        ScreenShotter.takeScreenshot(tag, activityRule.getActivity());
    }
}
