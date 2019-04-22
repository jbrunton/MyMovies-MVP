package com.jbrunton.mymovies.entities.repositories

import com.jbrunton.mymovies.entities.models.Genre

interface GenresRepository {
    fun genres(): DataStream<List<Genre>>
}
