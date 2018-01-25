package com.jbrunton.networking.resources.genres

import com.jbrunton.entities.Genre

data class GenresResponse(private val genres: List<GenreResource>) {

    data class GenreResource(private val id: String, private val name: String) {
        fun toGenre(): Genre {
            return Genre(id, name)
        }
    }

    fun toCollection(): List<Genre> {
        return genres.map { it.toGenre() }
    }
}
