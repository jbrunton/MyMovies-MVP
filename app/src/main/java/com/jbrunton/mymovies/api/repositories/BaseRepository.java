package com.jbrunton.mymovies.api.repositories;

import com.jbrunton.mymovies.api.DescriptiveError;

import java.util.function.Function;

public class BaseRepository {
    protected <T, R> T fromResult(R result, Function<R, T> transformer) {
        try {
            return transformer.apply(result);
        } catch (RuntimeException e) {
            throw new DescriptiveError(e.getMessage(), e, false);
        }
    }
}
