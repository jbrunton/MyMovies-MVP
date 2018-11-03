package com.jbrunton.networking.repositories

import com.jbrunton.entities.models.Genre
import com.jbrunton.entities.repositories.GenresRepository
import com.jbrunton.networking.services.MovieService
import io.reactivex.Observable

class HttpGenresRepository(private val service: MovieService) : GenresRepository {

    override fun genres(): Observable<List<Genre>> {
        return service.genres().map { it.toCollection() }
    }
}
