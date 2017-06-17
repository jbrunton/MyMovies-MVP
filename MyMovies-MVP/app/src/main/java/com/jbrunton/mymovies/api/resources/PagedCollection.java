package com.jbrunton.mymovies.api.resources;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class PagedCollection<T> {
    private Integer page;
    private Integer total_results;
    private Integer total_pages;
    private List<T> results;

    protected <S> List<S> toCollection(Function<T, S> converter) {
        return results.stream()
                .map(converter)
                .collect(Collectors.toList());
    }
}
