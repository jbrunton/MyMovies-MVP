package com.jbrunton.mymovies.converters;

import android.view.View;

public class VisibilityConverter {
    public static int toVisibility(boolean show) {
        return show ? View.VISIBLE : View.GONE;
    }
}
