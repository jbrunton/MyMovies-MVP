package com.jbrunton.mymovies.ui.discover.genres

import com.jbrunton.entities.models.Genre

data class GenreChipViewState(val genreName: String, val genreId: String, val selected: Boolean) {
    companion object {
        val Empty = GenreChipViewState("", "", false)
    }

    constructor(genre: Genre, selected: Boolean) : this(
            genreName = genre.name,
            genreId = genre.id,
            selected = selected)
}

typealias GenresViewState = List<GenreChipViewState>
