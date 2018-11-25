package com.jbrunton.mymovies

import com.jbrunton.entities.models.Genre
import com.jbrunton.entities.repositories.DataStream
import com.jbrunton.entities.repositories.GenresRepository

class TestGenresRepository : GenresRepository {
    override fun genres(): DataStream<List<Genre>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}