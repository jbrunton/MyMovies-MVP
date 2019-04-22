package com.jbrunton.mymovies.networking.repositories

import com.jbrunton.mymovies.entities.models.Genre
import com.jbrunton.mymovies.entities.repositories.DataStream
import com.jbrunton.mymovies.entities.repositories.GenresRepository
import com.jbrunton.mymovies.entities.repositories.toDataStream
import com.jbrunton.mymovies.networking.services.MovieService


class HttpGenresRepository(private val service: MovieService) : GenresRepository {
    override fun genres(): DataStream<List<Genre>> {
        return service.genres()
                .map { it.toCollection() }
                .toDataStream()
    }
}
