package com.jbrunton.entities.repositories

import com.jbrunton.entities.models.Genre
import io.reactivex.Observable

interface GenresRepository {
    fun genres(): Observable<List<Genre>>
}
