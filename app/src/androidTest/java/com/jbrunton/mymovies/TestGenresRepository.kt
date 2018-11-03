package com.jbrunton.mymovies

import com.jbrunton.entities.models.Genre
import com.jbrunton.entities.repositories.GenresRepository
import io.reactivex.Observable

class TestGenresRepository : GenresRepository {
    override fun genres(): Observable<List<Genre>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}