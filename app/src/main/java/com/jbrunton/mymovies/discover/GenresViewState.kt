package com.jbrunton.mymovies.discover

import com.jbrunton.entities.Genre
import com.jbrunton.mymovies.shared.LoadingViewState

data class GenresViewState(
        val loadingViewState: LoadingViewState,
        val genres: List<Genre>)
