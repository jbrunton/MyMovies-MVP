package com.jbrunton.mymovies.search

import android.support.annotation.DrawableRes
import com.jbrunton.mymovies.Movie
import com.jbrunton.mymovies.api.MaybeError
import java.util.*

data class SearchViewState(
        val movies: List<Movie>,
        val showError: Boolean,
        val errorMessage: String,
        val errorIcon: Int,
        val showTryAgainButton: Boolean = false)