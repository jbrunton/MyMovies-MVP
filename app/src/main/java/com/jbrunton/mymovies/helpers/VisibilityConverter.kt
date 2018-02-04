package com.jbrunton.mymovies.helpers

import android.view.View

fun toVisibility(show: Boolean): Int {
    return if (show) View.VISIBLE else View.GONE
}
