package com.jbrunton.mymovies.moviedetails;

import com.jbrunton.mymovies.movies.BaseMovieViewStateFactory;
import com.jbrunton.mymovies.movies.MovieViewState;
import com.jbrunton.mymovies.shared.LoadingViewState;
import com.jbrunton.entities.Movie;

public class MovieDetailsViewStateFactory extends BaseMovieViewStateFactory {
    public MovieDetailsViewState fromMovie(Movie movie) {
        return MovieDetailsViewState.builder()
                .setLoadingViewState(LoadingViewState.OK_STATE)
                .setMovie(toMovieViewState(movie))
                .build();
    }

    public MovieDetailsViewState fromError(Throwable throwable) {
        return MovieDetailsViewState.builder()
                .setLoadingViewState(loadingViewStateFactory.fromError(throwable))
                .setMovie(MovieViewState.EMPTY)
                .build();
    }

    public MovieDetailsViewState loadingState() {
        return MovieDetailsViewState.builder()
                .setLoadingViewState(LoadingViewState.LOADING_STATE)
                .setMovie(MovieViewState.EMPTY)
                .build();
    }

    private MovieViewState toMovieViewState(Movie movie) {
        return setDefaults(MovieViewState.builder(), movie)
                .overview(movie.overview().get())
                .build();
    }
}
