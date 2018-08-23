package com.jbrunton.mymovies;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.ProgressBar;

import com.jbrunton.entities.Movie;
import com.jbrunton.fixtures.MovieFactory;
import com.jbrunton.mymovies.fixtures.BaseTest;
import com.jbrunton.mymovies.fixtures.ProgressBarViewActions;
import com.jbrunton.mymovies.search.SearchFragment;
import com.jbrunton.mymovies.search.SearchViewState;
import com.jbrunton.mymovies.search.SearchViewStateFactory;
import com.jbrunton.networking.DescriptiveError;

import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.jbrunton.mymovies.fixtures.RecyclerViewUtils.withRecyclerView;
import static java.util.Arrays.asList;


@RunWith(AndroidJUnit4.class)
public class MainActivityTest extends BaseTest<MainActivity> {

    private static final SearchViewStateFactory FACTORY = new SearchViewStateFactory();

    private static final MovieFactory MOVIE_FACTORY = new MovieFactory();
    private static final Movie MOVIE1 = MOVIE_FACTORY.create();
    private static final Movie MOVIE2 = MOVIE_FACTORY.create();

    private static final SearchViewState EMPTY_STATE = FACTORY.getSearchEmptyState();
    private static final SearchViewState LOADING_STATE = FACTORY.getLoadingState();

    private static final DescriptiveError NETWORK_ERROR = new DescriptiveError("Network Error", true);

    @Test public void defaultsToSearchFragment() throws Exception {
        takeScreenshot();
        onView(withId(R.id.search_query)).check(matches(isDisplayed()));
    }

    @Test public void showsEmptySearchState() {
        setViewState(EMPTY_STATE);

        takeScreenshot("showsEmptySearchState");
        onView(withId(R.id.error_text))
                // TODO: externalize strings
                .check(matches(withText(EMPTY_STATE.getLoadingViewState().errorMessage())));
    }

    @Test public void showsLoadingState() {
        onView(isAssignableFrom(ProgressBar.class)).perform(ProgressBarViewActions.INSTANCE.replaceProgressBarDrawable());

        setViewState(LOADING_STATE);

        takeScreenshot("showsLoadingState");
        onView(withId(R.id.loading_indicator))
                .check(matches(isDisplayed()));
    }

    @Test public void showsErrorState() {
        setViewState(FACTORY.fromError(NETWORK_ERROR));

        takeScreenshot("showsErrorState");
        onView(withId(R.id.error_text))
                .check(matches(withText(NETWORK_ERROR.getMessage())));
        onView(withId(R.id.error_try_again))
                .check(matches(isDisplayed()));
    }

    @Test public void showsResults() {
        setViewState(FACTORY.fromList(asList(MOVIE1, MOVIE2)));

        takeScreenshot();

        onView(withRecyclerView(R.id.movies_list).atPosition(0))
                .check(matches(hasDescendant(withText(MOVIE1.getTitle()))));
        onView(withRecyclerView(R.id.movies_list).atPosition(1))
                .check(matches(hasDescendant(withText(MOVIE2.getTitle()))));
    }

    @Override protected ActivityTestRule<MainActivity> createActivityTestRule() {
        return new ActivityTestRule<>(MainActivity.class);
    }

    private SearchFragment searchFragment() {
        return (SearchFragment) getActivityRule().getActivity().getSupportFragmentManager().findFragmentById(R.id.content);
    }

    private void setViewState(SearchViewState viewState) {
        getActivityRule().getActivity().runOnUiThread(() -> searchFragment().updateView(viewState));
    }
}
