package com.jbrunton.mymovies.helpers;

import android.view.View;

public class VisibilityConverter {
    public static int toVisibility(boolean show) {
        return show ? View.VISIBLE : View.GONE;
    }
}
