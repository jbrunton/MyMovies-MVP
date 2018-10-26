package com.jbrunton.mymovies.discover

import com.jbrunton.entities.Genre
import com.jbrunton.mymovies.shared.LegacyLoadingViewState

data class GenresViewState(
        val loadingViewState: LegacyLoadingViewState,
        val genres: List<Genre>)
