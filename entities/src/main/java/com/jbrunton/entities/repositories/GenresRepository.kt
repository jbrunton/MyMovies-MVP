package com.jbrunton.entities.repositories

import com.jbrunton.entities.models.Genre

interface GenresRepository {
    fun genres(): DataStream<List<Genre>>
}
