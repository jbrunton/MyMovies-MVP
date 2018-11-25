package com.jbrunton.networking.repositories

import com.jbrunton.entities.models.Genre
import com.jbrunton.entities.repositories.DataStream
import com.jbrunton.entities.repositories.GenresRepository
import com.jbrunton.entities.repositories.toDataStream
import com.jbrunton.networking.services.MovieService


class HttpGenresRepository(private val service: MovieService) : GenresRepository {
    override fun genres(): DataStream<List<Genre>> {
        return service.genres()
                .map { it.toCollection() }
                .toDataStream()
    }
}
