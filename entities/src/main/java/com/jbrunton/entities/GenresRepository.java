package com.jbrunton.entities;

import java.util.List;

import io.reactivex.Observable;

public interface GenresRepository {
    Observable<List<Genre>> genres();
}
