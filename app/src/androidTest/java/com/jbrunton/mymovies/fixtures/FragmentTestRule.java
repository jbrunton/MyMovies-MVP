package com.jbrunton.mymovies.fixtures;

import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.test.rule.ActivityTestRule;

public class FragmentTestRule<A extends AppCompatActivity, F extends Fragment> extends ActivityTestRule<A> {
    private static final String TAG = "FragmentTestRule";

    private final Class<F> fragmentClass;
    private final boolean launchFragment;
    private F fragment;

    public static <F extends Fragment> FragmentTestRule<?, F> create(Class<F> fragmentClass) {
        return new FragmentTestRule<>(AppCompatActivity.class, fragmentClass);
    }

    public static <F extends Fragment> FragmentTestRule<?, F> create(Class<F> fragmentClass, boolean initialTouchMode) {
        return new FragmentTestRule<>(AppCompatActivity.class, fragmentClass, initialTouchMode);
    }

    public static <F extends Fragment> FragmentTestRule<?, F> create(Class<F> fragmentClass, boolean initialTouchMode, boolean launchFragment) {
        return new FragmentTestRule<>(AppCompatActivity.class, fragmentClass, initialTouchMode, true, launchFragment);
    }

    public FragmentTestRule(Class<A> activityClass, Class<F> fragmentClass) {
        this(activityClass, fragmentClass, false);
    }

    public FragmentTestRule(Class<A> activityClass, Class<F> fragmentClass, boolean initialTouchMode) {
        this(activityClass, fragmentClass, initialTouchMode, true);
    }

    public FragmentTestRule(Class<A> activityClass, Class<F> fragmentClass, boolean initialTouchMode, boolean launchActivity) {
        this(activityClass, fragmentClass, initialTouchMode, launchActivity, true);
    }

    public FragmentTestRule(Class<A> activityClass, Class<F> fragmentClass, boolean initialTouchMode, boolean launchActivity, boolean launchFragment) {
        super(activityClass, initialTouchMode, launchActivity);
        this.fragmentClass = fragmentClass;
        this.launchFragment = launchFragment;
    }

    @Override
    protected void afterActivityLaunched() {
        if (launchFragment) {
            launchFragment(createFragment());
        }
    }

    public void launchFragment(final F fragment) {
        try {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    final F fragment2 = fragment == null ? createFragment() : fragment;
                    FragmentTestRule.this.fragment = fragment2;
                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(android.R.id.content, fragment2)
                            .commitNow();
                }
            });
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }
    }

    protected F createFragment() {
        try {
            return fragmentClass.newInstance();
        } catch (InstantiationException e) {
            throw new AssertionError(String.format("%s: Could not insert %s into %s: %s",
                    getClass().getSimpleName(),
                    fragmentClass.getSimpleName(),
                    getActivity().getClass().getSimpleName(),
                    e.getMessage()));
        } catch (IllegalAccessException e) {
            throw new AssertionError(String.format("%s: Could not insert %s into %s: %s",
                    getClass().getSimpleName(),
                    fragmentClass.getSimpleName(),
                    getActivity().getClass().getSimpleName(),
                    e.getMessage()));
        }
    }

    public F getFragment() {
        if (fragment == null) {
            Log.w(TAG, "Fragment wasn't created yet");
        }
        return fragment;
    }
}