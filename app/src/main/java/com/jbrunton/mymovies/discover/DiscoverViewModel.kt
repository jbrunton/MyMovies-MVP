package com.jbrunton.mymovies.discover

import com.jbrunton.entities.repositories.MoviesRepository
import com.jbrunton.mymovies.search.BaseSearchViewModel

class DiscoverViewModel internal constructor(private val repository: MoviesRepository) : BaseSearchViewModel() {
    override fun start() {
        loadNowPlaying()
    }

    fun retry() {
        loadNowPlaying()
    }

    private fun loadNowPlaying() {
        search { repository.nowPlaying() }
    }
}
