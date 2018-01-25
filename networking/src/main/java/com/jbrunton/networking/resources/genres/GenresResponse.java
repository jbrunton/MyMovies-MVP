package com.jbrunton.networking.resources.genres;

import com.jbrunton.entities.Genre;

import java.util.List;
import java.util.stream.Collectors;

public class GenresResponse {
    private List<GenreResource> genres;

    private static class GenreResource {
        private String id;
        private String name;

        private static Genre toGenre(GenreResource resource) {
            return new Genre(resource.id, resource.name);
        }
    }

    public List<Genre> toCollection() {
        return genres.stream()
                .map(GenreResource::toGenre)
                .collect(Collectors.toList());
    }
}
