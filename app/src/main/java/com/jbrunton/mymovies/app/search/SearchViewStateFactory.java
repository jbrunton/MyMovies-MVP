package com.jbrunton.mymovies.app.search;

import com.google.common.collect.FluentIterable;
import com.jbrunton.entities.Movie;
import com.jbrunton.mymovies.R;
import com.jbrunton.mymovies.app.movies.BaseMovieViewStateFactory;
import com.jbrunton.mymovies.app.movies.MovieSearchResultViewState;
import com.jbrunton.mymovies.app.shared.LoadingViewState;

import java.util.Collections;
import java.util.List;

public class SearchViewStateFactory extends BaseMovieViewStateFactory {
    public SearchViewState fromList(List<Movie> movies) {
        if (movies.isEmpty()) {
            return fromLoadingViewState(LoadingViewState.errorBuilder()
                    .setErrorMessage("No Results")
                    .setErrorIcon(R.drawable.ic_search_black_24dp)
                    .build());
        } else {
            return SearchViewState.builder()
                    .setLoadingViewState(LoadingViewState.OK_STATE)
                    .setMovies(FluentIterable.from(movies).transform(this::toMovieSearchResultViewState).toList())
                    .build();
        }
    }

    public SearchViewState fromError(Throwable throwable) {
        return  fromLoadingViewState(loadingViewStateFactory.fromError(throwable));
    }

    public SearchViewState searchEmptyState() {
        return fromLoadingViewState(
                LoadingViewState.errorBuilder()
                .setErrorMessage("Search")
                .setErrorIcon(R.drawable.ic_search_black_24dp)
                .build());
    }

    public SearchViewState loadingState() {
        return fromLoadingViewState(LoadingViewState.LOADING_STATE);
    }

    private SearchViewState fromLoadingViewState(LoadingViewState loadingViewState) {
        return SearchViewState.builder()
                .setMovies(Collections.emptyList())
                .setLoadingViewState(loadingViewState)
                .build();
    }

    private MovieSearchResultViewState toMovieSearchResultViewState(Movie movie) {
        return setDefaults(MovieSearchResultViewState.builder(), movie)
                .build();
    }
}
