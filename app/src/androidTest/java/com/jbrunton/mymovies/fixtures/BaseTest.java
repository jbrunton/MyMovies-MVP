package com.jbrunton.mymovies.fixtures;

import android.app.Activity;
import android.support.test.rule.ActivityTestRule;

import com.google.android.libraries.cloudtesting.screenshots.ScreenShotter;
import com.squareup.spoon.SpoonRule;

import org.junit.Rule;

public abstract class BaseTest<T extends Activity> {
    @Rule public final ActivityTestRule<T> activityRule = createActivityTestRule();
    @Rule public final SpoonRule spoonRule = new SpoonRule();

    public void takeScreenshot() {
        takeScreenshot("_");
    }

    public void takeScreenshot(String tag) {
        spoonRule.screenshot(activityRule.getActivity(), tag);
        ScreenShotter.takeScreenshot(tag, activityRule.getActivity());
    }

    protected abstract ActivityTestRule<T> createActivityTestRule();
}
