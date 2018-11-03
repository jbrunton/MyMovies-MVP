package com.jbrunton.networking.repositories

import com.jbrunton.entities.models.DataStream
import com.jbrunton.entities.models.Genre
import com.jbrunton.entities.repositories.GenresRepository
import com.jbrunton.networking.services.MovieService


class HttpGenresRepository(private val service: MovieService) : BaseRepository(), GenresRepository {
    override fun genres(): DataStream<List<Genre>> {
        return buildResponse(service.genres().map { it.toCollection() })
    }
}
