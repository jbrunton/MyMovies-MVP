package com.jbrunton.mymovies.discover

import com.jbrunton.entities.MoviesRepository
import com.jbrunton.mymovies.search.BaseSearchViewModel

class GenreResultsViewModel(val genreId: String, val repository: MoviesRepository) : BaseSearchViewModel() {
    override fun start() {
        searchGenre()
    }

    fun retry() {
        searchGenre()
    }

    private fun searchGenre() {
        search { repository.discoverByGenre(genreId) }
    }
}
