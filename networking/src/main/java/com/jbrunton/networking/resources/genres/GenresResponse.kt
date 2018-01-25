package com.jbrunton.networking.resources.genres

import com.jbrunton.entities.Genre

class GenresResponse {
    private val genres: List<GenreResource>? = null

    private class GenreResource {
        private val id: String? = null
        private val name: String? = null

        fun toGenre(): Genre {
            return Genre(id!!, name!!)
        }
    }

    fun toCollection(): List<Genre> {
        return genres!!.map { it.toGenre() }
    }
}
