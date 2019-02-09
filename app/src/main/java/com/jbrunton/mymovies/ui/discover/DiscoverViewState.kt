package com.jbrunton.mymovies.ui.discover

import com.jbrunton.mymovies.ui.search.SearchViewState

data class DiscoverViewState(
        val nowPlayingViewState: SearchViewState,
        val popularViewState: SearchViewState
)