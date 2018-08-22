package com.jbrunton.entities

import io.reactivex.Observable

interface GenresRepository {
    fun genres(): Observable<List<Genre>>
}
