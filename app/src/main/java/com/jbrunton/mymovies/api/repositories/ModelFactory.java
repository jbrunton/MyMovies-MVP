package com.jbrunton.mymovies.api.repositories;

import com.jbrunton.mymovies.models.InvalidInstantiationException;

public interface ModelFactory<R, T> {
    T instantiate(R resource) throws InvalidInstantiationException;
}
