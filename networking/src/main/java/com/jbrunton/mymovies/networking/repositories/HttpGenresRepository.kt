package com.jbrunton.mymovies.networking.repositories

import com.jbrunton.mymovies.entities.models.Genre
import com.jbrunton.mymovies.entities.repositories.*
import com.jbrunton.mymovies.networking.services.MovieService


class HttpGenresRepository(private val service: MovieService) : GenresRepository {
    override suspend fun genres(): FlowDataStream<List<Genre>> {
        return buildFlowDataStream {
            service.genres().toCollection()
        }
    }
}
