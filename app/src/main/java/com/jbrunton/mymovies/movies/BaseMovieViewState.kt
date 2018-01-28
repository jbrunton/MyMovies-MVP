package com.jbrunton.mymovies.movies

interface BaseMovieViewState {
    val movieId: String
    val title: String
    val yearReleased: String
    val posterUrl: String
    val backdropUrl: String
    val rating: String

    open class Builder {
        var movieId: String = ""
        var title: String = ""
        var yearReleased: String = ""
        var posterUrl: String = ""
        var backdropUrl: String = ""
        var rating: String = ""
    }
}

