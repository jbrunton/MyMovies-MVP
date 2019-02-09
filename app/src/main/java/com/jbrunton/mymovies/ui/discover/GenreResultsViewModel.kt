package com.jbrunton.mymovies.ui.discover

import com.jbrunton.entities.repositories.MoviesRepository
import com.jbrunton.mymovies.ui.search.BaseSearchViewModel

class GenreResultsViewModel(val genreId: String, val repository: MoviesRepository) : BaseSearchViewModel() {
    override fun start() {
        searchGenre()
    }

    override fun retry() {
        searchGenre()
    }

    private fun searchGenre() {
        search { repository.discoverByGenre(genreId) }
    }
}
