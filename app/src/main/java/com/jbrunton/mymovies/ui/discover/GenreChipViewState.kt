package com.jbrunton.mymovies.ui.discover

import com.jbrunton.entities.models.Genre

data class GenreChipViewState(val genre: Genre, val selected: Boolean) {
    companion object {
        val Empty = GenreChipViewState(Genre("", ""), false)
    }
}

typealias GenresViewState = List<GenreChipViewState>
