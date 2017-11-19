package com.jbrunton.mymovies;

import android.support.test.runner.AndroidJUnit4;

import com.jbrunton.mymovies.api.DescriptiveError;
import com.jbrunton.mymovies.app.search.SearchFragment;
import com.jbrunton.mymovies.app.search.SearchViewState;
import com.jbrunton.mymovies.app.search.SearchViewStateFactory;
import com.jbrunton.mymovies.fixtures.ScreenshotTest;

import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;


@RunWith(AndroidJUnit4.class)
public class MainActivityTest extends ScreenshotTest {
    private final SearchViewStateFactory factory = new SearchViewStateFactory();

    @Test public void defaultsToSearchFragment() throws Exception {
        takeScreenshot();
        onView(withId(R.id.search_query)).check(matches(isDisplayed()));
    }

    @Test public void showsEmptySearchState() {
        SearchViewState viewState = factory.searchEmptyState();
        activityRule.getActivity().runOnUiThread(() -> searchFragment().updateView(viewState));

        takeScreenshot("showsEmptySearchState");
        onView(withId(R.id.error_text))
                // TODO: externalize strings
                .check(matches(withText(viewState.loadingViewState().errorMessage())));
    }

    @Test public void showsLoadingState() {
        SearchViewState viewState = factory.loadingState();
        activityRule.getActivity().runOnUiThread(() -> searchFragment().updateView(viewState));

        takeScreenshot("showsLoadingState");
        onView(withId(R.id.loading_indicator))
                .check(matches(isDisplayed()));
    }

    @Test public void showsErrorState() {
        SearchViewState viewState = factory.fromError(new DescriptiveError("Network Error", true));
        activityRule.getActivity().runOnUiThread(() -> searchFragment().updateView(viewState));

        takeScreenshot("showsErrorState");
        onView(withId(R.id.error_text))
                .check(matches(withText("Invalid Error")));
        onView(withId(R.id.error_try_again))
                .check(matches(isDisplayed()));
    }

    private SearchFragment searchFragment() {
        return (SearchFragment) activityRule.getActivity().getSupportFragmentManager().findFragmentById(R.id.content);
    }
}
