package com.jbrunton.mymovies.moviedetails;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.jbrunton.entities.Movie;
import com.jbrunton.entities.MoviesRepository;
import com.jbrunton.mymovies.shared.BaseViewModel;

public class MovieDetailsViewModel extends BaseViewModel {
    private final MutableLiveData<MovieDetailsViewState> viewState = new MutableLiveData<>();
    private final MoviesRepository repository;
    private final String movieId;
    private final MovieDetailsViewStateFactory viewStateFactory = new MovieDetailsViewStateFactory();

    public MovieDetailsViewModel(String movieId, MoviesRepository repository) {
        this.movieId = movieId;
        this.repository = repository;
    }

    public LiveData<MovieDetailsViewState> viewState() {
        return viewState;
    }


    @Override public void start() {
        loadDetails();
    }

    public void retry() {
        loadDetails();
    }

    private void loadDetails() {
        viewState.setValue(viewStateFactory.loadingState());
        repository.getMovie(movieId)
                .compose(applySchedulers())
                .subscribe(this::setMovieResponse, this::setErrorResponse);
    }

    private void setMovieResponse(Movie movie) {
        viewState.setValue(viewStateFactory.fromMovie(movie));
    }

    private void setErrorResponse(Throwable throwable) {
        viewState.setValue(viewStateFactory.fromError(throwable));
    }

    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        private final String movieId;
        private final MoviesRepository repository;

        public Factory(String movieId, MoviesRepository repository) {
            this.movieId = movieId;
            this.repository = repository;
        }

        @Override public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new MovieDetailsViewModel(movieId, repository);
        }
    }
}